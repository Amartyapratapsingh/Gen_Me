package com.example.genme

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.genme.R
import com.example.genme.ui.ErrorCard
import com.example.genme.ui.GlasmorphicPageHeader
import com.example.genme.ui.HtmlStyleBottomNav
import com.example.genme.ui.ImageUploadCard
import com.example.genme.ui.ResultCard
import com.example.genme.ui.TransformButton
import com.example.genme.utils.rememberImagePicker
import com.example.genme.viewmodel.TryOnViewModel
import com.example.genme.viewmodel.TryOnViewModelFactory

@Composable
fun GhibliArtPage(navController: NavController) {
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Glassmorphic header matching home page
                    GlasmorphicPageHeader(
                        title = "Ghibli Art Maker",
                        subtitle = "Create magical Studio Ghibli art with AI",
                        navController = navController,
                        primaryColor = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    // Coming soon content
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gibli),
                        contentDescription = "Coming Soon",
                        tint = primaryColor,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Coming Soon!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "We're working hard to bring the magic of Ghibli to you. Stay tuned for updates!",
                        fontSize = 18.sp,
                        color = textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Bottom navigation
                HtmlStyleBottomNav(
                    primaryColor = primaryColor,
                    textSecondary = textSecondary,
                    navController = navController,
                    currentRoute = "ghibli_art"
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
