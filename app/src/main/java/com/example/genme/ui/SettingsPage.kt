package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genme.viewmodel.TryOnViewModel

@Composable
fun SettingsPage(
    navController: NavController,
    viewModel: TryOnViewModel
) {
    val background = Color(0xFF111122)
    val glass = Color.White.copy(alpha = 0.05f)
    val border = Color.White.copy(alpha = 0.10f)

    val blue = Color(0xFF00A9FF)
    val purple = Color(0xFFA076F9)
    val cyan = Color(0xFF00F5D4)

    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedVersion by remember { mutableStateOf("Gen ME v1.3") }
    var versionMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Soft ambient gradient blobs
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(blue.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, purple.copy(alpha = 0.10f))
                    )
                )
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
                    .padding(horizontal = 16.dp)
            ) {
                // Header: Back + Title centered
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .size(44.dp)
                            .clickable { navController.popBackStack() },
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.10f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(44.dp))
                }

                // General Settings
                SectionTitle("General Settings")
                GlassCard(borderColor = blue.copy(alpha = 0.20f)) {
                    // Notifications toggle
                    ToggleRow(
                        iconGradient = Brush.linearGradient(listOf(blue, cyan)),
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        checked = notificationsEnabled,
                        onChange = { notificationsEnabled = it }
                    )
                    Divider(color = Color.White.copy(alpha = 0.10f))

                    // Gen ME Version with dropdown and NEW badge
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconBox(Brush.linearGradient(listOf(blue, purple))) {
                                    Icon(Icons.Default.Memory, contentDescription = null, tint = Color.White)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Box { // Title + badge overlay container
                                    Text(
                                        text = "Gen ME Version",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 56.dp, y = (-8).dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Brush.horizontalGradient(listOf(purple, cyan)))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("NEW", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { versionMenuExpanded = true }
                            ) {
                                Text(selectedVersion, color = Color(0xFFCBD5E1))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFFCBD5E1))
                                DropdownMenu(expanded = versionMenuExpanded, onDismissRequest = { versionMenuExpanded = false }) {
                                    listOf("Gen ME v1.0", "Gen ME v1.2", "Gen ME v1.3").forEach { v ->
                                        DropdownMenuItem(
                                            text = { Text(v) },
                                            onClick = { selectedVersion = v; versionMenuExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Divider(color = Color.White.copy(alpha = 0.10f))

                    // Language picker (navigable)
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(blue, cyan)),
                        icon = Icons.Default.Language,
                        title = "Language",
                        value = "English",
                        onClick = { /* TODO: Navigate to language */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy & Security
                SectionTitle("Privacy & Security")
                GlassCard(borderColor = purple.copy(alpha = 0.20f)) {
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(purple, blue)),
                        icon = Icons.Default.Security,
                        title = "Privacy Settings",
                        onClick = { /* TODO: Navigate */ }
                    )
                    Divider(color = Color.White.copy(alpha = 0.10f))
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(purple, blue)),
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        onClick = { /* TODO: Navigate */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Help & Support
                SectionTitle("Help & Support")
                GlassCard(borderColor = blue.copy(alpha = 0.20f)) {
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(cyan, blue)),
                        icon = Icons.Default.HelpOutline,
                        title = "Help Center",
                        onClick = { /* TODO */ }
                    )
                    Divider(color = Color.White.copy(alpha = 0.10f))
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(cyan, blue)),
                        icon = Icons.Default.Email,
                        title = "Contact Us",
                        onClick = { /* TODO */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // About Us
                SectionTitle("About Us")
                GlassCard(borderColor = purple.copy(alpha = 0.20f)) {
                    NavRow(
                        iconGradient = Brush.linearGradient(listOf(purple, cyan)),
                        icon = Icons.Default.Info,
                        title = "About Gen ME",
                        onClick = { /* TODO */ }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
            // Bottom bar is provided globally via MainActivity Scaffold (unchanged)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    )
}

@Composable
private fun GlassCard(borderColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            content()
        }
    }
}

@Composable
private fun IconBox(gradient: Brush, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(gradient),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun ToggleRow(
    iconGradient: Brush,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBox(iconGradient) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1313EC),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
private fun NavRow(
    iconGradient: Brush,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBox(iconGradient) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            value?.let { Text(it, color = Color(0xFFCBD5E1)) }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF9CA3AF))
        }
    }
}

