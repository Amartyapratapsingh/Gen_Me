package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.genme.R
import com.example.genme.viewmodel.TryOnViewModel
import java.io.File

import androidx.navigation.NavController

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: TryOnViewModel,
    navController: NavController
) {
    val images by viewModel.galleryImages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Ensure gallery refreshes whenever this screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadGalleryImages()
    }

    // Also refresh when the screen is resumed from back stack
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadGalleryImages()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val bg = Color(0xFF111122)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        // Sticky Header
        GalleryHeader(
            title = "My Outfits",
            onBack = { navController.popBackStack() }
        )

        // Grid content
        Box(modifier = Modifier.weight(1f)) {
            if (uiState.errorMessage != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "Failed to load images.", color = Color.White)
                }
            } else if (images.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No images found.", color = Color.White)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(images) { index, imageFile ->
                        val ring = when (index % 3) {
                            0 -> Color(0xFF8B5CF6) // purple-500
                            1 -> Color(0xFF22D3EE) // cyan-400/500
                            else -> Color(0xFF3B82F6) // blue-500
                        }
                        GalleryTile(
                            imageFile = imageFile,
                            ringColor = ring,
                            onClick = {
                                val encodedPath = URLEncoder.encode(imageFile.absolutePath, StandardCharsets.UTF_8.toString())
                                navController.navigate("full_screen_image/$encodedPath")
                            }
                        )
                    }
                }
            }
        }

        // Bottom nav provided by MainActivity Scaffold
    }
}

@Composable
private fun GalleryTile(imageFile: File, ringColor: Color, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape)
            .border(1.dp, Color.White.copy(0.1f), shape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = imageFile)
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Gradient overlay from bottom
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(0.6f), Color.Transparent)
                    )
                )
        )
        // Glow ring overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(1.dp, ringColor.copy(alpha = 0.5f), shape)
        )
    }
}

@Composable
private fun GalleryHeader(
    title: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color(0xCC111122))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(40.dp))
        }
    }
}

@Composable
private fun GalleryFooterNav(
    onHome: () -> Unit,
    onStyles: () -> Unit,
    onGenerate: () -> Unit,
    onCreations: () -> Unit,
    onSettings: () -> Unit
) {
    val barBg = Color(0xCC111122)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(barBg)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterItem(icon = Icons.Filled.Home, label = "Home", selected = false, onClick = onHome)
        FooterItem(iconRes = R.drawable.ic_clothing, label = "Styles", selected = false, onClick = onStyles)

        // Center action
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(100))
                .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF22D3EE))))
                .clickable { onGenerate() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_gibli),
                contentDescription = "Generate",
                tint = Color.White
            )
        }

        FooterItem(iconRes = R.drawable.ic_gallery, label = "Creations", selected = true, onClick = onCreations)
        FooterItem(icon = Icons.Filled.Settings, label = "Settings", selected = false, onClick = onSettings)
    }
}

@Composable
private fun FooterItem(
    icon: ImageVector? = null,
    iconRes: Int? = null,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) Color.White else Color(0xFF9292C9)
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = label, tint = color)
        } else if (iconRes != null) {
            Icon(painter = painterResource(id = iconRes), contentDescription = label, tint = color)
        }
        Spacer(Modifier.height(4.dp))
        Text(text = label, color = color, style = MaterialTheme.typography.labelSmall)
    }
}
