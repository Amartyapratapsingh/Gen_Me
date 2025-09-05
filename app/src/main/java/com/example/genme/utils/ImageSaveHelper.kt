package com.example.genme.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for saving images to the device gallery
 */
class ImageSaveHelper(private val context: Context) {
    
    /**
     * Save a bitmap to the device gallery
     * Returns true if successful, false otherwise
     */
    suspend fun saveImageToGallery(
        bitmap: Bitmap,
        filename: String = "GenMe_${System.currentTimeMillis()}.jpg",
        mimeType: String = "image/jpeg",
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 90
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val resolver: ContentResolver = context.contentResolver
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore API for Android 10+ (Scoped Storage)
                saveImageToGalleryModern(resolver, bitmap, filename, mimeType, compressFormat, quality)
            } else {
                // Use legacy method for older Android versions
                saveImageToGalleryLegacy(bitmap, filename, compressFormat, quality)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Save image using MediaStore API (Android 10+)
     */
    private suspend fun saveImageToGalleryModern(
        resolver: ContentResolver,
        bitmap: Bitmap,
        filename: String,
        mimeType: String,
        compressFormat: Bitmap.CompressFormat,
        quality: Int
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GenMe")
                
                // Add metadata
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
            
            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            
            imageUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(compressFormat, quality, outputStream)
                }
                Result.success(uri)
            } ?: Result.failure(Exception("Failed to create image URI"))
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Save image using legacy method (Android 9 and below)
     */
    private suspend fun saveImageToGalleryLegacy(
        bitmap: Bitmap,
        filename: String,
        compressFormat: Bitmap.CompressFormat,
        quality: Int
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val genMeDir = File(picturesDir, "GenMe")
            
            if (!genMeDir.exists()) {
                genMeDir.mkdirs()
            }
            
            val imageFile = File(genMeDir, filename)
            val outputStream: OutputStream = FileOutputStream(imageFile)
            
            bitmap.compress(compressFormat, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            
            // Add to MediaStore so it appears in gallery immediately
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                imageFile.absolutePath,
                filename,
                "GenMe AI Virtual Try-On Result"
            )
            
            Result.success(Uri.fromFile(imageFile))
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Generate a unique filename with timestamp
     */
    fun generateFilename(prefix: String = "GenMe"): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "${prefix}_TryOn_${timestamp}.jpg"
    }
}

/**
 * Composable function to remember ImageSaveHelper instance
 */
@Composable
fun rememberImageSaveHelper(context: Context): ImageSaveHelper {
    return remember { ImageSaveHelper(context) }
}

