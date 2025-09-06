package com.example.genme.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.example.genme.R
import com.example.genme.viewmodel.TryOnUiState
import com.example.genme.viewmodel.HairstyleUiState

@Composable
fun TryOnProgressIndicator(uiState: TryOnUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(3) { index ->
            val isActive = when (index) {
                0 -> true // Image selection is always first step
                1 -> uiState.hasAllImages || uiState.isLoading || uiState.hasResult
                2 -> uiState.hasResult
                else -> false
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (isActive) Color(0xFF00D4FF) else Color(0xFF374151),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun HairstyleProgressIndicator(uiState: HairstyleUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(3) { index ->
            val isActive = when (index) {
                0 -> true // Image selection is always first step
                1 -> uiState.hasAllInputs || uiState.isLoading || uiState.hasResult
                2 -> uiState.hasResult
                else -> false
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (isActive) Color(0xFF00D4FF) else Color(0xFF374151),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun ErrorCard(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        border = BorderStroke(1.5.dp, Color(0xFFEF4444)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFEF4444).copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }
            
            IconButton(onClick = onDismiss) {
                Text(
                    text = "✕",
                    fontSize = 16.sp,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
fun ImageUploadCard(
    title: String,
    subtitle: String,
    description: String,
    borderColor: Color,
    icon: Int,
    selectedImageUri: android.net.Uri?,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (selectedImageUri != null) 200.dp else 120.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        border = BorderStroke(1.5.dp, if (enabled) borderColor else Color(0xFF374151)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        if (selectedImageUri != null) {
            // Show selected image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF161B22),
                                Color(0xFF0D1117)
                            )
                        )
                    )
            ) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Success indicator
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(20.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp).size(16.dp)
                    )
                }
            }
        } else {
            // Show upload prompt
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF161B22),
                                Color(0xFF0D1117)
                            )
                        )
                    )
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (enabled) Color.White else Color(0xFF6B7280)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (enabled) borderColor else Color(0xFF6B7280)
                    )
                    Text(
                        text = description,
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                Card(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF161B22)
                    ),
                    border = BorderStroke(1.dp, if (enabled) borderColor else Color(0xFF374151))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = title,
                            tint = borderColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransformButton(
    enabled: Boolean,
    isLoading: Boolean,
    loadingMessage: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = enabled && !isLoading) { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        border = BorderStroke(
            2.dp, 
            if (enabled && !isLoading) Color(0xFF00D4FF) else Color(0xFF374151)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled && !isLoading) {
                            listOf(
                                Color(0xFF00D4FF).copy(alpha = 0.1f),
                                Color(0xFF8B5CF6).copy(alpha = 0.1f),
                                Color(0xFF00D4FF).copy(alpha = 0.1f)
                            )
                        } else {
                            listOf(
                                Color(0xFF374151).copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF00D4FF),
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = loadingMessage ?: "Processing...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00D4FF)
                    )
                }
            } else {
                Text(
                    text = if (enabled) "✨ TRANSFORM WITH AI ✨" else "SELECT IMAGES TO CONTINUE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color(0xFF00F5FF) else Color(0xFF6B7280),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun ResultCard(
    resultImage: Bitmap,
    taskId: String?,
    isSaving: Boolean = false,
    saveSuccessMessage: String? = null,
    onReset: () -> Unit,
    onSaveToGallery: () -> Unit,
    onClearSaveMessage: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        border = BorderStroke(1.5.dp, Color(0xFF06D6A0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF06D6A0).copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color(0xFF06D6A0),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "TRANSFORMATION COMPLETE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF06D6A0)
                )
            }
            
            // Result image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    bitmap = resultImage.asImageBitmap(),
                    contentDescription = "Try-on result",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Task ID (for debugging)
            if (taskId != null) {
                Text(
                    text = "Task ID: $taskId",
                    fontSize = 10.sp,
                    color = Color(0xFF6B7280),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
            
            // Save success message
            saveSuccessMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF10B981))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Saved",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = message,
                                fontSize = 12.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        IconButton(
                            onClick = onClearSaveMessage,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Text(
                                text = "✕",
                                fontSize = 12.sp,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Save to Gallery button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable(enabled = !isSaving) { onSaveToGallery() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF161B22)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF8B5CF6))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSaving) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color(0xFF8B5CF6),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "SAVING...",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8B5CF6),
                                    letterSpacing = 1.sp
                                )
                            }
                        } else {
                            Text(
                                text = "💾 SAVE TO GALLERY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B5CF6),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
                
                // Try Another Style button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable { onReset() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF161B22)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF06D6A0))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✨ TRY ANOTHER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF06D6A0),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuturisticDropdown(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Label
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (enabled) Color(0xFF00D4FF) else Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Dropdown trigger
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { expanded = !expanded },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D1117)
            ),
            border = BorderStroke(
                1.5.dp, 
                if (enabled) {
                    if (expanded) Color(0xFF00D4FF) else Color(0xFF374151)
                } else Color(0xFF2D3748)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (enabled && expanded) {
                                listOf(
                                    Color(0xFF00D4FF).copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color(0xFF161B22),
                                    Color(0xFF0D1117)
                                )
                            }
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedValue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) Color.White else Color(0xFF6B7280),
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = if (enabled) Color(0xFF00D4FF) else Color(0xFF6B7280),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        // Dropdown menu
        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0D1117)
                ),
                border = BorderStroke(1.5.dp, Color(0xFF00D4FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF161B22),
                                    Color(0xFF0D1117)
                                )
                            )
                        )
                        .padding(4.dp)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Search...", color = Color(0xFF6B7280)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF00D4FF),
                            unfocusedIndicatorColor = Color(0xFF374151)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyColumn {
                        items(options.filter { it.contains(searchText, ignoreCase = true) }) { option ->
                            val isSelected = option == selectedValue
                            
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .clickable {
                                        onValueChange(option)
                                        expanded = false
                                        searchText = ""
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) {
                                        Color(0xFF00D4FF).copy(alpha = 0.2f)
                                    } else {
                                        Color.Transparent
                                    }
                                ),
                                border = if (isSelected) {
                                    BorderStroke(1.dp, Color(0xFF00D4FF))
                                } else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFF00D4FF) else Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFF00D4FF),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlasmorphicPageHeader(
    title: String,
    subtitle: String,
    navController: NavController,
    primaryColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button with glassmorphic style
        Card(
            modifier = Modifier
                .size(48.dp)
                .clickable { navController.popBackStack() },
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80282828)
            ),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Title section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
        
        // Spacer to balance the layout
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun HtmlStyleBottomNav(
    primaryColor: Color,
    textSecondary: Color,
    navController: NavController,
    currentRoute: String = ""
) {
    // Bottom nav with glassmorphic card styling
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Margin around the card
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp), // Rounded corners
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80282828) // Glassmorphic background
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
                    .padding(horizontal = 8.dp, vertical = 8.dp) // Inner padding
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HtmlBottomNavItem(
                        iconType = "home",
                        label = "Home",
                        isSelected = currentRoute == "landing_page" || currentRoute == "home" || currentRoute.isEmpty(),
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { 
                            navController.navigate("landing_page") {
                                // Clear the back stack to prevent crashes when going back to home
                                popUpTo("landing_page") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    
                    HtmlBottomNavItem(
                        iconType = "gallery",
                        label = "Gallery",
                        isSelected = currentRoute == "gallery",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { 
                            navController.navigate("gallery") {
                                launchSingleTop = true
                            }
                        }
                    )
                    
                    HtmlBottomNavItem(
                        iconType = "profile",
                        label = "Profile",
                        isSelected = currentRoute == "coins",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = { 
                            navController.navigate("coins") {
                                launchSingleTop = true
                            }
                        }
                    )
                    
                    HtmlBottomNavItem(
                        iconType = "settings",
                        label = "Settings",
                        isSelected = currentRoute == "settings",
                        primaryColor = primaryColor,
                        textSecondary = textSecondary,
                        onClick = {
                            navController.navigate("settings") {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HtmlBottomNavItem(
    iconType: String,
    label: String,
    isSelected: Boolean,
    primaryColor: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                color = if (isSelected) primaryColor.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Custom icons to match design
        HtmlBottomNavIcon(
            iconType = iconType,
            tint = if (isSelected) primaryColor else textSecondary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) primaryColor else textSecondary
        )
    }
}

@Composable
fun HtmlBottomNavIcon(iconType: String, tint: Color) {
    when (iconType) {
        "home" -> {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        "gallery" -> {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_gallery),
                contentDescription = "Gallery",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        "profile" -> {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        "settings" -> {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
