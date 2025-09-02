package com.example.genme.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.genme.api.VirtualTryOnApiService
import com.example.genme.api.models.TryOnStartResponse
import com.example.genme.api.models.TryOnStatus
import com.example.genme.api.models.TryOnStatusResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Repository class for handling Virtual Try-On API operations
 */
class VirtualTryOnRepository(private val context: Context) {
    
    companion object {
        private const val BASE_URL = "http://35.232.52.16/"
        private const val POLL_INTERVAL_MS = 3000L // Poll every 3 seconds
        private const val MAX_POLL_ATTEMPTS = 60 // Max 3 minutes of polling
    }
    
    private val apiService: VirtualTryOnApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VirtualTryOnApiService::class.java)
    }
    
    /**
     * Start a virtual try-on job and return the task ID
     */
    suspend fun startTryOn(personImageUri: Uri, clothingImageUri: Uri): Result<String> {
        return try {
            val personImagePart = createImagePart("person_image", personImageUri)
            val clothingImagePart = createImagePart("clothing_image", clothingImageUri)
            
            val response = apiService.startTryOn(personImagePart, clothingImagePart)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.taskId)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get the current status of a try-on job
     */
    suspend fun getJobStatus(taskId: String): Result<TryOnStatusResponse> {
        return try {
            val response = apiService.getStatus(taskId)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Download the result image for a completed try-on job
     */
    suspend fun downloadResult(taskId: String): Result<Bitmap> {
        return try {
            val response = apiService.getResult(taskId)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val inputStream = body.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        Result.success(bitmap)
                    } else {
                        Result.failure(Exception("Failed to decode image"))
                    }
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Start a try-on job and poll for completion, emitting status updates
     */
    fun startTryOnWithPolling(personImageUri: Uri, clothingImageUri: Uri): Flow<TryOnResult> = flow {
        emit(TryOnResult.Loading("Starting virtual try-on..."))
        
        // Start the try-on job
        val startResult = startTryOn(personImageUri, clothingImageUri)
        if (startResult.isFailure) {
            emit(TryOnResult.Error(startResult.exceptionOrNull()?.message ?: "Failed to start try-on"))
            return@flow
        }
        
        val taskId = startResult.getOrThrow()
        emit(TryOnResult.Loading("Processing images... Task ID: $taskId"))
        
        // Poll for completion
        var attempts = 0
        while (attempts < MAX_POLL_ATTEMPTS) {
            delay(POLL_INTERVAL_MS)
            attempts++
            
            val statusResult = getJobStatus(taskId)
            if (statusResult.isFailure) {
                emit(TryOnResult.Error(statusResult.exceptionOrNull()?.message ?: "Failed to check status"))
                return@flow
            }
            
            val status = statusResult.getOrThrow()
            when (status.status) {
                TryOnStatus.PROCESSING -> {
                    emit(TryOnResult.Loading("Processing... (${attempts * POLL_INTERVAL_MS / 1000}s)"))
                }
                TryOnStatus.COMPLETED -> {
                    emit(TryOnResult.Loading("Downloading result..."))
                    val resultBitmap = downloadResult(taskId)
                    if (resultBitmap.isSuccess) {
                        emit(TryOnResult.Success(resultBitmap.getOrThrow(), taskId))
                    } else {
                        emit(TryOnResult.Error(resultBitmap.exceptionOrNull()?.message ?: "Failed to download result"))
                    }
                    return@flow
                }
                TryOnStatus.FAILED -> {
                    emit(TryOnResult.Error(status.error ?: status.message ?: "Try-on job failed"))
                    return@flow
                }
            }
        }
        
        // If we reach here, we've exceeded max attempts
        emit(TryOnResult.Error("Try-on job timed out. Please try again."))
    }
    
    /**
     * Create a MultipartBody.Part from an image URI
     */
    private fun createImagePart(name: String, imageUri: Uri): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val file = File(context.cacheDir, "${name}_${System.currentTimeMillis()}.jpg")
        
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }
    
    /**
     * Check API health
     */
    suspend fun checkHealth(): Result<Boolean> {
        return try {
            val response = apiService.healthCheck()
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Sealed class representing the result of a try-on operation
 */
sealed class TryOnResult {
    data class Loading(val message: String) : TryOnResult()
    data class Success(val resultImage: Bitmap, val taskId: String) : TryOnResult()
    data class Error(val message: String) : TryOnResult()
}
