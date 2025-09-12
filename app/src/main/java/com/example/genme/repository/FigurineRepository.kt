package com.example.genme.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.genme.api.VirtualTryOnApiService
import com.example.genme.api.models.TryOnStatus
import com.example.genme.api.models.TryOnStatusResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import org.json.JSONObject

class FigurineRepository(private val context: Context) {
    companion object {
        private const val BASE_URL = "http://35.232.52.16/"
        private const val POLL_INTERVAL_MS = 3000L
        private const val MAX_POLL_ATTEMPTS = 60
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val apiService: VirtualTryOnApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VirtualTryOnApiService::class.java)
    }

    suspend fun startFigurine(personImageUri: Uri, styleText: String): Result<String> {
        return try {
            val characterImagePart = createImagePart("character_image", personImageUri)
            val styleKeyPart = styleText.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.startFigurine(characterImagePart, styleKeyPart)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body.taskId) else Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobStatus(taskId: String): Result<TryOnStatusResponse> {
        return try {
            val first = apiService.getStatus(taskId)
            if (first.isSuccessful) {
                val body = first.body()
                if (body != null) Result.success(body) else Result.failure(Exception("Empty response body"))
            } else if (first.code() == 404 || first.code() == 405) {
                val alt = apiService.getFigurineStatus(taskId)
                if (alt.isSuccessful) {
                    alt.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("API error: ${alt.code()} ${alt.message()}"))
                }
            } else {
                Result.failure(Exception("API error: ${first.code()} ${first.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadResult(taskId: String): Result<Bitmap> {
        return try {
            val response = apiService.getResult(taskId)
            val finalResponse = if (!response.isSuccessful && (response.code() == 404 || response.code() == 405)) {
                apiService.getFigurineResult(taskId)
            } else response

            if (finalResponse.isSuccessful) {
                val body = finalResponse.body()
                if (body != null) {
                    val contentType = body.contentType()?.toString()?.lowercase() ?: ""
                    if (contentType.contains("image")) {
                        val bitmap = BitmapFactory.decodeStream(body.byteStream())
                        if (bitmap != null) Result.success(bitmap) else Result.failure(Exception("Failed to decode image"))
                    } else {
                        // JSON or other: try to parse URL and fetch image
                        val text = body.string()
                        val url = extractResultUrl(text)
                        if (url != null) {
                            val fetched = fetchBitmapFromUrl(url)
                            if (fetched != null) Result.success(fetched) else Result.failure(Exception("Failed to download image from URL"))
                        } else {
                            Result.failure(Exception("No image content and no result URL in response"))
                        }
                    }
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API error: ${finalResponse.code()} ${finalResponse.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun startFigurineWithPolling(personImageUri: Uri, styleText: String): Flow<FigurineResult> = flow {
        emit(FigurineResult.Loading("Starting 3D figurine generation..."))

        val startResult = startFigurine(personImageUri, styleText)
        if (startResult.isFailure) {
            emit(FigurineResult.Error(startResult.exceptionOrNull()?.message ?: "Failed to start generation"))
            return@flow
        }

        val taskId = startResult.getOrThrow()
        emit(FigurineResult.Loading("Processing... Task ID: $taskId"))

        var attempts = 0
        while (attempts < MAX_POLL_ATTEMPTS) {
            delay(POLL_INTERVAL_MS)
            attempts++

            val statusResult = getJobStatus(taskId)
            if (statusResult.isFailure) {
                emit(FigurineResult.Error(statusResult.exceptionOrNull()?.message ?: "Failed to check status"))
                return@flow
            }

            val status = statusResult.getOrThrow()
            when (status.status) {
                TryOnStatus.PROCESSING -> emit(FigurineResult.Loading("Processing... (${attempts * POLL_INTERVAL_MS / 1000}s)"))
                TryOnStatus.COMPLETED -> {
                    emit(FigurineResult.Loading("Downloading result..."))
                    // If backend provided a result URL in status, use it directly
                    val directUrl = status.resultUrl
                    val resultBitmap = if (!directUrl.isNullOrBlank()) {
                        val bmp = fetchBitmapFromUrl(directUrl)
                        if (bmp != null) Result.success(bmp) else Result.failure(Exception("Failed to fetch image from result URL"))
                    } else {
                        downloadResult(taskId)
                    }
                    if (resultBitmap.isSuccess) emit(FigurineResult.Success(resultBitmap.getOrThrow(), taskId))
                    else emit(FigurineResult.Error(resultBitmap.exceptionOrNull()?.message ?: "Failed to download result"))
                    return@flow
                }
                TryOnStatus.FAILED -> {
                    emit(FigurineResult.Error(status.error ?: status.message ?: "Figurine job failed"))
                    return@flow
                }
            }
        }
        emit(FigurineResult.Error("Generation timed out. Please try again."))
    }

    private fun createImagePart(name: String, imageUri: Uri): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val file = File(context.cacheDir, "${name}_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input -> FileOutputStream(file).use { output -> input.copyTo(output) } }
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }

    private fun extractResultUrl(jsonText: String): String? {
        return try {
            val obj = JSONObject(jsonText)
            when {
                obj.has("result_url") -> obj.getString("result_url")
                obj.has("result") -> obj.getString("result")
                obj.has("image_url") -> obj.getString("image_url")
                obj.has("url") -> obj.getString("url")
                obj.has("output_url") -> obj.getString("output_url")
                else -> null
            }
        } catch (e: Exception) { null }
    }

    private fun fetchBitmapFromUrl(url: String): Bitmap? {
        return try {
            val request = okhttp3.Request.Builder().url(url).get().build()
            httpClient.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body ?: return null
                BitmapFactory.decodeStream(body.byteStream())
            }
        } catch (_: Exception) { null }
    }
}

sealed class FigurineResult {
    data class Loading(val message: String) : FigurineResult()
    data class Success(val resultImage: Bitmap, val taskId: String) : FigurineResult()
    data class Error(val message: String) : FigurineResult()
}
