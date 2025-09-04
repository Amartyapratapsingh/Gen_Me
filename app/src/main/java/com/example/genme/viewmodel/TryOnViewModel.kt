package com.example.genme.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.genme.repository.TryOnResult
import com.example.genme.repository.VirtualTryOnRepository
import com.example.genme.utils.ImageSaveHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel for managing virtual try-on operations and UI state
 */
class TryOnViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VirtualTryOnRepository(application)
    private val imageSaveHelper = ImageSaveHelper(application)

    private val _uiState = MutableStateFlow(TryOnUiState())
    val uiState: StateFlow<TryOnUiState> = _uiState.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<File>>(emptyList())
    val galleryImages: StateFlow<List<File>> = _galleryImages.asStateFlow()

    init {
        loadGalleryImages()
    }

    /**
     * Set the person image URI
     */
    fun setPersonImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(personImageUri = uri)
    }

    /**
     * Set the clothing image URI
     */
    fun setClothingImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(clothingImageUri = uri)
    }

    /**
     * Start the virtual try-on process
     */
    fun startTryOn() {
        val currentState = _uiState.value
        val personUri = currentState.personImageUri
        val clothingUri = currentState.clothingImageUri

        if (personUri == null || clothingUri == null) {
            _uiState.value = currentState.copy(
                errorMessage = "Please select both person and clothing images"
            )
            return
        }

        viewModelScope.launch {
            repository.startTryOnWithPolling(personUri, clothingUri)
                .collect { result ->
                    when (result) {
                        is TryOnResult.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                loadingMessage = result.message,
                                errorMessage = null
                            )
                        }
                        is TryOnResult.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                resultImage = result.resultImage,
                                taskId = result.taskId,
                                loadingMessage = null,
                                errorMessage = null
                            )
                        }
                        is TryOnResult.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = result.message,
                                loadingMessage = null
                            )
                        }
                    }
                }
        }
    }
    
    /**
     * Clear the error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Clear the selected images
     */
    fun clearImages() {
        _uiState.value = _uiState.value.copy(personImageUri = null, clothingImageUri = null, resultImage = null)
    }

    /**
     * Reset the try-on process
     */
    fun reset() {
        _uiState.value = TryOnUiState()
    }

    /**
     * Check if ready to start try-on
     */
    fun isReadyToStart(): Boolean {
        val state = _uiState.value
        return state.personImageUri != null &&
               state.clothingImageUri != null &&
               !state.isLoading
    }

    /**
     * Check API health
     */
    fun checkApiHealth() {
        viewModelScope.launch {
            val result = repository.checkHealth()
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(isApiHealthy = true)
            } else {
                _uiState.value.copy(
                    isApiHealthy = false,
                    errorMessage = "API is not available. Please try again later."
                )
            }
        }
    }

    /**
     * Save the result image to gallery
     */
    fun saveResultToGallery() {
        val currentState = _uiState.value
        val resultBitmap = currentState.resultImage

        if (resultBitmap == null) {
            _uiState.value = currentState.copy(
                errorMessage = "No result image to save"
            )
            return
        }

        viewModelScope.launch {
            // Show saving state
            _uiState.value = currentState.copy(isSaving = true)

            val filename = imageSaveHelper.generateFilename("GenMe")
            val result = imageSaveHelper.saveImageToGallery(resultBitmap, filename)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveSuccessMessage = "Image saved to gallery successfully!",
                    savedImageUri = result.getOrNull()
                )
                // Refresh gallery images
                loadGalleryImages()
            } else {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Failed to save image: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    /**
     * Clear save success message
     */
    fun clearSaveMessage() {
        _uiState.value = _uiState.value.copy(saveSuccessMessage = null)
    }

    /**
     * Load images from the "GenMe" directory
     */
    fun loadGalleryImages() {
        viewModelScope.launch {
            try {
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val genMeDir = File(picturesDir, "GenMe")
                if (genMeDir.exists() && genMeDir.isDirectory) {
                    val images = genMeDir.listFiles { file ->
                        file.isFile && (file.extension.equals("jpg", ignoreCase = true) ||
                                         file.extension.equals("png", ignoreCase = true))
                    }?.sortedByDescending { it.lastModified() }
                    _galleryImages.value = images ?: emptyList()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to load gallery images.")
            }
        }
    }
}

/**
 * Factory for creating a [TryOnViewModel] with a constructor that takes an [Application].
 */
class TryOnViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TryOnViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TryOnViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Data class representing the UI state for virtual try-on
 */
data class TryOnUiState(
    val personImageUri: Uri? = null,
    val clothingImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val loadingMessage: String? = null,
    val resultImage: Bitmap? = null,
    val taskId: String? = null,
    val errorMessage: String? = null,
    val isApiHealthy: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccessMessage: String? = null,
    val savedImageUri: Uri? = null
) {
    /**
     * Check if both images are selected
     */
    val hasAllImages: Boolean
        get() = personImageUri != null && clothingImageUri != null

    /**
     * Check if try-on has completed successfully
     */
    val hasResult: Boolean
        get() = resultImage != null

    /**
     * Check if ready to start try-on
     */
    val canStartTryOn: Boolean
        get() = hasAllImages && !isLoading && isApiHealthy
}