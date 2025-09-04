package com.example.genme

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.genme.ui.ErrorCard
import com.example.genme.ui.ImageUploadCard
import com.example.genme.ui.ResultCard
import com.example.genme.ui.TransformButton
import com.example.genme.utils.rememberImagePicker
import com.example.genme.viewmodel.TryOnViewModel
import com.example.genme.viewmodel.TryOnViewModelFactory

@Composable
fun GhibliArtPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: TryOnViewModel = remember {
        ViewModelProvider(context as androidx.activity.ComponentActivity, TryOnViewModelFactory(context.applicationContext as Application)).get(TryOnViewModel::class.java)
    }
    val uiState by viewModel.uiState.collectAsState()

    val imagePicker = rememberImagePicker { uri ->
        uri?.let { viewModel.setPersonImage(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB), // Sky Blue
                        Color(0xFF98FB98), // Pale Green
                        Color(0xFFFFFACD)  // Lemon Chiffon
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            FuturisticPageHeader(navController, uiState.isApiHealthy)
            Spacer(modifier = Modifier.height(32.dp))
            GhibliArtPageTitle()
            Spacer(modifier = Modifier.height(32.dp))

            uiState.errorMessage?.let { errorMessage ->
                ErrorCard(
                    message = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            ImageUploadCard(
                title = "Your Image",
                subtitle = "Upload a photo",
                description = "Upload an image to be transformed into Ghibli art",
                borderColor = Color(0xFF6B8E23), // Olive Drab
                icon = "🎨",
                selectedImageUri = uiState.personImageUri,
                onClick = imagePicker,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            TransformButton(
                enabled = uiState.personImageUri != null && !uiState.isLoading,
                isLoading = uiState.isLoading,
                loadingMessage = uiState.loadingMessage,
                onClick = { /* Implement Ghibli art transformation logic */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.hasResult) {
                ResultCard(
                    resultImage = uiState.resultImage!!,
                    taskId = uiState.taskId,
                    isSaving = uiState.isSaving,
                    saveSuccessMessage = uiState.saveSuccessMessage,
                    onReset = { viewModel.reset() },
                    onSaveToGallery = { viewModel.saveResultToGallery() },
                    onClearSaveMessage = { viewModel.clearSaveMessage() }
                )
            }
        }
    }
}

@Composable
fun GhibliArtPageTitle() {
    Text(
        text = "Ghibli Art Studio",
        style = MaterialTheme.typography.headlineLarge,
        color = Color(0xFF2F4F4F) // Dark Slate Gray
    )
}
