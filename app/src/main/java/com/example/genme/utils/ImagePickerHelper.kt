package com.example.genme.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Composable function to handle image picking from gallery
 */
@Composable
fun rememberImagePicker(
    onImageSelected: (Uri?) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }
    
    return {
        launcher.launch("image/*")
    }
}

/**
 * Data class to hold image picker state
 */
data class ImagePickerState(
    val selectedImageUri: Uri? = null,
    val isPickerOpen: Boolean = false
)

/**
 * Composable function to manage image picker state
 */
@Composable
fun rememberImagePickerState(): ImagePickerState {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isPickerOpen by remember { mutableStateOf(false) }
    
    return ImagePickerState(
        selectedImageUri = selectedImageUri,
        isPickerOpen = isPickerOpen
    )
}
