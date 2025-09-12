package com.example.genme.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.genme.repository.FigurineRepository
import com.example.genme.repository.FigurineResult
import com.example.genme.utils.ImageSaveHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FigurineUiState(
    val personImageUri: Uri? = null,
    val selectedStyle: String = "",
    val isLoading: Boolean = false,
    val loadingMessage: String? = null,
    val resultImage: Bitmap? = null,
    val taskId: String? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val saveSuccessMessage: String? = null,
    val savedImageUri: Uri? = null
) {
    val hasAllInputs: Boolean get() = personImageUri != null && selectedStyle.isNotBlank()
    val hasResult: Boolean get() = resultImage != null
}

class FigurineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FigurineRepository(application)
    private val imageSaveHelper = ImageSaveHelper(application)

    private val _uiState = MutableStateFlow(FigurineUiState())
    val uiState: StateFlow<FigurineUiState> = _uiState.asStateFlow()

    fun setPersonImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(personImageUri = uri)
    }

    fun setStyle(style: String) {
        _uiState.value = _uiState.value.copy(selectedStyle = style)
    }

    fun startGeneration() {
        val state = _uiState.value
        val image = state.personImageUri
        val style = state.selectedStyle

        if (image == null) {
            _uiState.value = state.copy(errorMessage = "Please select a photo")
            return
        }
        if (style.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please select a style")
            return
        }

        viewModelScope.launch {
            repository.startFigurineWithPolling(image, style).collect { result ->
                when (result) {
                    is FigurineResult.Loading -> _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        loadingMessage = result.message,
                        errorMessage = null
                    )
                    is FigurineResult.Success -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resultImage = result.resultImage,
                        taskId = result.taskId,
                        loadingMessage = null,
                        errorMessage = null
                    )
                    is FigurineResult.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        loadingMessage = null
                    )
                }
            }
        }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(errorMessage = null) }
    fun reset() { _uiState.value = FigurineUiState() }

    fun saveResultToGallery() {
        val state = _uiState.value
        val bitmap = state.resultImage
        if (bitmap == null) {
            _uiState.value = state.copy(errorMessage = "No result to save")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            val filename = imageSaveHelper.generateFilename("GenMe_Figurine")
            val result = imageSaveHelper.saveImageToGallery(bitmap, filename)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(isSaving = false, saveSuccessMessage = "Saved to gallery", savedImageUri = result.getOrNull())
            } else {
                _uiState.value.copy(isSaving = false, errorMessage = result.exceptionOrNull()?.message ?: "Save failed")
            }
        }
    }
}

class FigurineViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FigurineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FigurineViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

