package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun SettingsPage(
    navController: NavController,
    viewModel: TryOnViewModel
) {
    // Colors matching the rest of the app
    val primaryColor = Color(0xFF38E07B)
    val backgroundColor = Color(0xFF121212)
    val textSecondary = Color(0xFFB3B3B3)
    val accentColor = Color(0xFF5CE690)

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(true) }
    var highQualityMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Dark overlay + subtle colorful gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            ColorfulBackdrop(primaryColor = primaryColor, accentColor = accentColor)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                )
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                GlasmorphicPageHeader(
                    title = "Settings",
                    subtitle = "Customize your experience",
                    navController = navController,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // General Settings Section
                SettingsSection(
                    title = "General",
                    primaryColor = primaryColor
                ) {
                    SettingsToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Receive app notifications",
                        isChecked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        primaryColor = primaryColor,
                        textSecondary = textSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsToggleItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        isChecked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it },
                        primaryColor = primaryColor,
                        textSecondary = textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App Settings Section
                SettingsSection(
                    title = "App Settings",
                    primaryColor = primaryColor
                ) {
                    SettingsToggleItem(
                        icon = Icons.Default.HighQuality,
                        title = "High Quality Mode",
                        subtitle = "Better generation quality (slower)",
                        isChecked = highQualityMode,
                        onCheckedChange = { highQualityMode = it },
                        primaryColor = primaryColor,
                        textSecondary = textSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsItem(
                        icon = Icons.Default.Storage,
                        title = "Storage Management",
                        subtitle = "Manage app data and cache",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { /* TODO: Navigate to storage settings */ }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = "Language",
                        subtitle = "English (US)",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { /* TODO: Navigate to language settings */ }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // About Section
                SettingsSection(
                    title = "About",
                    primaryColor = primaryColor
                ) {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About GenMe",
                        subtitle = "Version 1.0.0",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { /* TODO: Show about dialog */ }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsItem(
                        icon = Icons.Default.Policy,
                        title = "Privacy Policy",
                        subtitle = "View our privacy policy",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { /* TODO: Navigate to privacy policy */ }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsItem(
                        icon = Icons.Default.ContactSupport,
                        title = "Contact Support",
                        subtitle = "Get help and support",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { /* TODO: Navigate to support */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Navigation
            HtmlStyleBottomNav(
                primaryColor = primaryColor,
                textSecondary = textSecondary,
                navController = navController,
                currentRoute = "settings"
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    primaryColor: Color,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x80282828),
                                Color(0x60202020)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    primaryColor: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
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
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
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
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    primaryColor: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
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
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = textSecondary
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = primaryColor,
                checkedTrackColor = primaryColor.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}
