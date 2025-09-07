package com.example.genme.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.genme.repository.HairstyleResult
import com.example.genme.repository.HairstyleRepository
import com.example.genme.utils.ImageSaveHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import android.os.Environment

/**
 * ViewModel for managing hairstyle change operations and UI state
 */
class HairstyleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HairstyleRepository(application)
    private val imageSaveHelper = ImageSaveHelper(application)

    private val _uiState = MutableStateFlow(HairstyleUiState())
    val uiState: StateFlow<HairstyleUiState> = _uiState.asStateFlow()

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
     * Set the selected hairstyle text
     */
    fun setHairstyleText(hairstyle: String) {
        _uiState.value = _uiState.value.copy(selectedHairstyle = hairstyle)
    }

    /**
     * Start the hairstyle change process
     */
    fun startHairstyleChange() {
        val currentState = _uiState.value
        val personUri = currentState.personImageUri
        val hairstyleText = currentState.selectedHairstyle

        if (personUri == null) {
            _uiState.value = currentState.copy(
                errorMessage = "Please select a person image"
            )
            return
        }

        if (hairstyleText.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please select a hairstyle"
            )
            return
        }

        viewModelScope.launch {
            repository.startHairstyleChangeWithPolling(personUri, hairstyleText)
                .collect { result ->
                    when (result) {
                        is HairstyleResult.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                loadingMessage = result.message,
                                errorMessage = null
                            )
                        }
                        is HairstyleResult.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                resultImage = result.resultImage,
                                taskId = result.taskId,
                                loadingMessage = null,
                                errorMessage = null
                            )
                        }
                        is HairstyleResult.Error -> {
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
     * Clear the selected images and result
     */
    fun clearImages() {
        _uiState.value = _uiState.value.copy(
            personImageUri = null, 
            resultImage = null
        )
    }

    /**
     * Reset the hairstyle change process
     */
    fun reset() {
        _uiState.value = HairstyleUiState()
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

            val filename = imageSaveHelper.generateFilename("GenMe_Hairstyle")
            val result = imageSaveHelper.saveImageToGallery(resultBitmap, filename)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveSuccessMessage = "Hairstyle image saved to gallery successfully!",
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
 * Factory for creating a [HairstyleViewModel] with a constructor that takes an [Application].
 */
class HairstyleViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HairstyleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HairstyleViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Data class representing the UI state for hairstyle change
 */
data class HairstyleUiState(
    val personImageUri: Uri? = null,
    val selectedHairstyle: String = "",
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
     * Check if all required inputs are provided
     */
    val hasAllInputs: Boolean
        get() = personImageUri != null && selectedHairstyle.isNotBlank()

    /**
     * Check if hairstyle change has completed successfully
     */
    val hasResult: Boolean
        get() = resultImage != null

    /**
     * Check if ready to start hairstyle change
     */
    val canStartHairstyleChange: Boolean
        get() = hasAllInputs && !isLoading && isApiHealthy
}



