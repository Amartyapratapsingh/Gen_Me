package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genme.viewmodel.TryOnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    navController: NavController,
    viewModel: TryOnViewModel
) {
    // Define colors matching homepage
    val primaryColor = Color(0xFF00C851)
    val backgroundColor = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFFB0B0B0)
    val accentColor = Color(0xFF00FF5F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Background overlay matching homepage
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            GlasmorphicPageHeader(
                title = "Profile",
                subtitle = "Manage your account",
                navController = navController,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Profile Avatar Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x80282828)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x80282828),
                                    Color(0x60202020)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Profile Picture
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            primaryColor.copy(alpha = 0.3f),
                                            primaryColor.copy(alpha = 0.1f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                tint = primaryColor,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Welcome User",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "user@genme.app",
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Options
            ProfileOption(
                icon = Icons.Default.Edit,
                title = "Edit Profile",
                subtitle = "Update your personal information",
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                onClick = { /* TODO: Navigate to edit profile */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOption(
                icon = Icons.Default.History,
                title = "Generation History",
                subtitle = "View your past creations",
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                onClick = { navController.navigate("gallery") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOption(
                icon = Icons.Default.Star,
                title = "Favorites",
                subtitle = "Your saved generations",
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                onClick = { /* TODO: Navigate to favorites */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOption(
                icon = Icons.Default.Share,
                title = "Share App",
                subtitle = "Tell friends about GenMe",
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                onClick = { /* TODO: Implement sharing */ }
            )

            Spacer(modifier = Modifier.weight(1f))
            // Bottom nav provided by MainActivity Scaffold
        }
    }
}

@Composable
fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    primaryColor: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80282828)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x80282828),
                            Color(0x60202020)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = textSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
