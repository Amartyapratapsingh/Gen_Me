package com.example.genme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ColorfulBackdrop(
    primaryColor: Color = Color(0xFF38E07B),
    accentColor: Color = Color(0xFF5CE690)
) {
    // Layer soft gradients to create a subtle, colorful background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.20f),
                        Color.Transparent
                    )
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        primaryColor.copy(alpha = 0.15f)
                    )
                )
            )
    )
}

