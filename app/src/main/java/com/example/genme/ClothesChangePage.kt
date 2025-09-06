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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.example.genme.viewmodel.TryOnViewModel
import kotlin.math.*
import kotlin.random.Random

import androidx.lifecycle.ViewModelProvider
import com.example.genme.viewmodel.TryOnViewModelFactory

@Composable
fun ClothesChangePage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: TryOnViewModel = remember {
        ViewModelProvider(context as androidx.activity.ComponentActivity, TryOnViewModelFactory(context.applicationContext as Application)).get(TryOnViewModel::class.java)
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
    
    val clothingImagePicker = rememberImagePicker { uri ->
        uri?.let { viewModel.setClothingImage(it) }
    }
    
    // Neon palette to mirror provided HTML
    val backgroundColor = Color(0xFF0D0C14)
    val textSecondary = Color(0xFF9CA3AF)
    val cyan = Color(0xFF22D3EE)   // tailwind cyan-400
    val purple = Color(0xFF9333EA) // tailwind purple-600
    val primaryGlow = listOf(cyan.copy(alpha = 0.4f), purple.copy(alpha = 0.4f))
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background exactly matching homepage - FULL SCREEN NO GAPS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        )
        
        // Dark overlay + subtle colorful gradients (matches HTML soft blobs)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            // Soft directional gradients for ambient color
            com.example.genme.ui.ColorfulBackdrop(
                primaryColor = cyan,
                accentColor = purple
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
            // Minimal header like HTML (back + centered title)
            HtmlLikeHeader(
                title = "Generate",
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Progress indicator (kept, though not shown in HTML)
            TryOnProgressIndicator(uiState)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Error message
            uiState.errorMessage?.let { errorMessage ->
                ErrorCard(
                    message = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Upload rows styled like HTML glass cards
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GlassUploadRow(
                    title = "Your Image",
                    subtitle = "Upload a photo",
                    iconRes = R.drawable.ic_person,
                    selectedImageUri = uiState.personImageUri,
                    onClick = personImagePicker,
                    accent = cyan,
                    enabled = !uiState.isLoading
                )
                GlassUploadRow(
                    title = "Outfit Image",
                    subtitle = "Upload inspiration",
                    iconRes = R.drawable.ic_clothing,
                    selectedImageUri = uiState.clothingImageUri,
                    onClick = clothingImagePicker,
                    accent = purple,
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Generate button with neon gradient + glow
            NeonGenerateButton(
                text = "Generate",
                enabled = uiState.canStartTryOn,
                loading = uiState.isLoading,
                loadingMessage = uiState.loadingMessage,
                gradient = Brush.horizontalGradient(listOf(cyan, purple)),
                onClick = { viewModel.startTryOn() }
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
            
            }
            // Bottom nav provided by MainActivity Scaffold
        }
        }
    }
}


@Composable
fun FuturisticPageHeader(navController: NavController, isApiHealthy: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Futuristic back button
        Card(
            modifier = Modifier
                .size(48.dp)
                .clickable { navController.popBackStack() },
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D1117)
            ),
            border = BorderStroke(
                width = 1.5.dp,
                color = Color(0xFF00D4FF)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF00D4FF).copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF00F5FF),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        
        // Futuristic status indicator
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D1117)
            ),
            border = BorderStroke(
                width = 1.5.dp,
                color = Color(0xFF06D6A0)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF161B22),
                                Color(0xFF0D1117),
                                Color(0xFF161B22)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pulseAlpha by rememberInfiniteTransition(label = "pulse").animateFloat(
                        initialValue = 0.6f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ), label = "alpha"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (isApiHealthy) 
                                    Color(0xFF06D6A0).copy(alpha = pulseAlpha) 
                                else 
                                    Color(0xFFEF4444).copy(alpha = pulseAlpha),
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                ambientColor = if (isApiHealthy) 
                                    Color(0xFF06D6A0).copy(alpha = 0.6f)
                                else
                                    Color(0xFFEF4444).copy(alpha = 0.6f)
                            )
                    )
                    
                    Text(
                        text = if (isApiHealthy) "NEURAL AI READY" else "API OFFLINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isApiHealthy) Color(0xFF06D6A0) else Color(0xFFEF4444),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FuturisticPageTitle() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = Color(0xFF00D4FF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF161B22),
                            Color(0xFF0D1117)
                        )
                    )
                )
                .padding(28.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // "Neural" with futuristic cyan gradient
                Text(
                    text = "Neural",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.W300,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00F5FF), // Bright cyan
                                Color(0xFF00D4FF), // Cyan
                                Color(0xFF0891B2)  // Darker cyan
                            )
                        )
                    ),
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
                
                // "Style" with futuristic gold gradient
                Text(
                    text = "Style",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFFBEB), // Very light yellow
                                Color(0xFFFBBF24), // Golden yellow
                                Color(0xFFEAB308)  // Darker gold
                            )
                        )
                    ),
                    letterSpacing = 1.8.sp,
                    textAlign = TextAlign.Center
                )
                
                // "Transfer" with futuristic purple gradient
                Text(
                    text = "Transfer",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE0E7FF), // Light purple
                                Color(0xFF8B5CF6), // Purple
                                Color(0xFF6D28D9)  // Darker purple
                            )
                        )
                    ),
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "AI-POWERED IMAGE TRANSFORMATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF00D4FF).copy(alpha = 0.8f),
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HtmlLikeHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back arrow
        Card(
            modifier = Modifier
                .size(44.dp)
                .clickable { onBack() },
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(44.dp))
    }
}

 

@Composable
private fun GlassUploadRow(
    title: String,
    subtitle: String,
    iconRes: Int,
    selectedImageUri: android.net.Uri?,
    onClick: () -> Unit,
    accent: Color,
    enabled: Boolean
) {
    val borderColor = if (enabled) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.05f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (selectedImageUri != null) 200.dp else 96.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, borderColor)
    ) {
        if (selectedImageUri != null) {
            // Show selected image preview
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                // subtle overlay border
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            color = Color(0xFF9CA3AF),
                            fontSize = 13.sp
                        )
                    }
                }
                // Trailing circular add button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Add",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NeonGenerateButton(
    text: String,
    enabled: Boolean,
    loading: Boolean,
    loadingMessage: String?,
    gradient: Brush,
    onClick: () -> Unit
) {
    val glowCyan = Color(0x8000F6FF)
    val glowPurple = Color(0x804B0082)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 24.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (enabled) glowPurple else Color.Transparent,
                spotColor = if (enabled) glowCyan else Color.Transparent
            )
            .clickable(enabled = enabled && !loading) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = loadingMessage ?: "Generating...",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
