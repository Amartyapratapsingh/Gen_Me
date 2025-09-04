package com.example.genme

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.genme.R
import com.example.genme.ui.*
import com.example.genme.utils.rememberImagePicker
import com.example.genme.viewmodel.HairstyleViewModel
import com.example.genme.viewmodel.HairstyleViewModelFactory
import kotlin.math.*
import kotlin.random.Random

import androidx.lifecycle.ViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HairstyleChangePage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: HairstyleViewModel = remember {
        ViewModelProvider(context as androidx.activity.ComponentActivity, HairstyleViewModelFactory(context.applicationContext as Application)).get(HairstyleViewModel::class.java)
    }
    val uiState by viewModel.uiState.collectAsState()
    
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearImages()
        }
    }
    
    // Initialize API health check
    LaunchedEffect(Unit) {
        viewModel.checkApiHealth()
    }
    
    // Image pickers
    val personImagePicker = rememberImagePicker { uri ->
        uri?.let { viewModel.setPersonImage(it) }
    }
    
    // Colors exactly matching homepage
    val primaryColor = Color(0xFF38E07B)
    val backgroundColor = Color(0xFF121212)
    val textSecondary = Color(0xFFB3B3B3)
    val accentColor = Color(0xFF5CE690)
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background exactly matching homepage - FULL SCREEN NO GAPS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        )
        
        // Dark overlay exactly like homepage - FULL SCREEN + subtle colorful gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            com.example.genme.ui.ColorfulBackdrop(primaryColor = primaryColor, accentColor = accentColor)
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
            // Glassmorphic header matching home page
            GlasmorphicPageHeader(
                title = "Hairstyle Changer",
                subtitle = "Find your perfect hairstyle with AI",
                navController = navController,
                primaryColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Progress indicator
            HairstyleProgressIndicator(uiState)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Error message
            uiState.errorMessage?.let { errorMessage ->
                ErrorCard(
                    message = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Upload cards
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Person Image Card
                ImageUploadCard(
                    title = "Your Image",
                    subtitle = "Upload a photo",
                    description = "Upload an image to try on a new hairstyle",
                    borderColor = Color(0xFF00D4FF),
                    icon = R.drawable.ic_person,
                    selectedImageUri = uiState.personImageUri,
                    onClick = personImagePicker,
                    enabled = !uiState.isLoading
                )
                
                // Hairstyle selection dropdown - Futuristic style
                val hairstyles = listOf(
                    "Pixie Cut", "Bob Cut", "Lob Cut", "Shag Cut", "Mullet", "Buzz Cut", "Crew Cut", "Ivy League",
                    "Slicked Back", "Pompadour", "Quiff", "Faux Hawk", "Afro", "Cornrows", "Dreadlocks", "High Top Fade",
                    "Box Braids", "Twists", "Bantu Knots", "Finger Waves", "Curtain Bangs", "Blunt Bangs", "Side-Swept Bangs",
                    "Wispy Bangs", "Long Layers", "Short Layers", "Feathered Layers", "Asymmetrical Cut", "Undercut",
                    "Man Bun", "Top Knot", "Half-Up Half-Down", "Ponytail", "Pigtails", "French Braid", "Dutch Braid",
                    "Fishtail Braid", "Waterfall Braid", "Crown Braid", "Messy Bun", "Space Buns", "Chignon", "Beehive",
                    "Victory Rolls", "Pin Curls", "Bouffant", "Flip", "Emo", "Scene"
                )
                var selectedHairstyle by remember { mutableStateOf(hairstyles[0]) }
                
                // Initialize hairstyle text in ViewModel
                LaunchedEffect(Unit) {
                    viewModel.setHairstyleText(hairstyles[0])
                }
                
                FuturisticDropdown(
                    selectedValue = selectedHairstyle,
                    options = hairstyles,
                    label = "Hairstyle",
                    onValueChange = { 
                        selectedHairstyle = it
                        viewModel.setHairstyleText(it)
                    },
                    enabled = !uiState.isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Transform button
            TransformButton(
                enabled = uiState.canStartHairstyleChange,
                isLoading = uiState.isLoading,
                loadingMessage = uiState.loadingMessage,
                onClick = { viewModel.startHairstyleChange() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Result section
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
            
            Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
            }
            
            // Bottom navigation
            HtmlStyleBottomNav(
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                navController = navController,
                currentRoute = "hairstyle_change"
            )
        }
        }
    }
}
