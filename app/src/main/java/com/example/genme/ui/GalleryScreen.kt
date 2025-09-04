package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.genme.viewmodel.TryOnViewModel
import java.io.File
import com.example.genme.ui.FuturisticBottomNav

import androidx.navigation.NavController

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: TryOnViewModel,
    navController: NavController
) {
    val images by viewModel.galleryImages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

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
            // Glassmorphic header matching other pages
            GlasmorphicPageHeader(
                title = "Gallery",
                subtitle = "Your saved creations",
                navController = navController,
                primaryColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                if (uiState.errorMessage != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.errorMessage ?: "Failed to load images.", color = Color.White)
                    }
                } else if (images.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No images found.", color = Color.White)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(images) { imageFile ->
                            GlasmorphicImageCard(
                                imageFile = imageFile,
                                onClick = {
                                    val encodedPath = URLEncoder.encode(imageFile.absolutePath, StandardCharsets.UTF_8.toString())
                                    navController.navigate("full_screen_image/$encodedPath")
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp)) // Space for bottom nav
            }
            
            // Bottom navigation
            HtmlStyleBottomNav(
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                navController = navController,
                currentRoute = "gallery"
            )
        }
        }
    }
}

@Composable
fun GlasmorphicImageCard(imageFile: File, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80282828) // Glassmorphic background
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x80282828),
                            Color(0x60202020)
                        )
                    )
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = imageFile)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
