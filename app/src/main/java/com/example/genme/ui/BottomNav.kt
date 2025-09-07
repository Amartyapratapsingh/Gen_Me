package com.example.genme.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genme.R

@Composable
fun StitchBottomNav(
    navController: NavController,
    currentRoute: String?
) {
    // Working colors from hairstyle changer page
    val unselected = Color.Gray // Exact same as working hairstyle nav
    val selected = Color.White
    
    // Working background from hairstyle changer page  
    val glassBg = Color.Black.copy(alpha = 0.3f) // This exact line works perfectly in hairstyle page

    Column(modifier = Modifier.fillMaxWidth()) {
        // HTML equivalent: border-t border-white/10
        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
        
        // EXACT working implementation from hairstyle page middle navigation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.3f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
            // Home
            StitchNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == "landing_page",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = { navController.navigate("landing_page") }
            )

            // Gallery
            StitchNavItem(
                icon = ImageVector.vectorResource(id = R.drawable.ic_gallery),
                label = "Gallery",
                selected = currentRoute == "gallery",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = { navController.navigate("gallery") }
            )

            // Generate (center highlighted) - HTML: bg-gradient-to-tr from-cyan-400 to-purple-600
            StitchCenterGenerateItem(
                selected = currentRoute == "ghibli_art",
                gradient = Brush.radialGradient(
                    colors = listOf(Color(0xFF22D3EE), Color(0xFF9333EA)), // cyan-400 to purple-600
                    radius = 100f
                ),
                onClick = { navController.navigate("ghibli_art") }
            )

            // Profile button routes to Coins page
            StitchNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == "coins",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = { navController.navigate("coins") }
            )

            // Settings
            StitchNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                selected = currentRoute == "settings",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = { navController.navigate("settings") }
            )
            }
        }
        
        // Bottom safe area with matching glassmorphism effect (HTML: h-5 bg-black/30)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun StitchNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit
) {
    val tint = if (selected) selectedColor else unselectedColor
    
    // EXACT structure from working hairstyle page navigation
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = tint,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StitchCenterGenerateItem(
    selected: Boolean,
    gradient: Brush,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.offset(y = (-24).dp)
    ) {
        // HTML exact: p-3 rounded-full shadow-[0_0_15px_rgba(0,246,255,0.5)]
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(100),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(100),
                ambientColor = Color(0x8000F6FF), // Exact HTML rgba(0,246,255,0.5)
                spotColor = Color(0x8000F6FF)     // Same cyan glow for consistency
            )
        ) {
            Box(
                modifier = Modifier
                    .background(gradient, shape = RoundedCornerShape(100))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generate",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Generate",
            color = Color.White,
            fontSize = 12.sp, // Same as working hairstyle nav
            fontWeight = FontWeight.Medium // Same as working hairstyle nav
        )
    }
}

@Composable
fun FuturisticBottomNav(navController: NavController, currentRoute: String? = "landing_page") {
    val borderGlow by rememberInfiniteTransition(label = "nav_glow").animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color(0xFF00D4FF).copy(alpha = 0.3f),
                spotColor = Color(0xFF6366F1).copy(alpha = 0.2f)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF161B22),
                            Color(0xFF0D1117),
                            Color(0xFF161B22)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FuturisticBottomNavItem("Home", currentRoute == "landing_page", onClick = { navController.navigate("landing_page") })
                FuturisticBottomNavItem("Gallery", currentRoute == "gallery", onClick = { navController.navigate("gallery") })
            }
        }
    }
}


@Composable
fun HtmlStyleBottomNav(
    primaryColor: Color,
    textSecondary: Color,
    navController: NavController,
    currentRoute: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HtmlStyleNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            isSelected = currentRoute == "landing_page",
            primaryColor = primaryColor,
            textSecondary = textSecondary,
            onClick = { navController.navigate("landing_page") }
        )
        HtmlStyleNavItem(
            label = "Settings",
            icon = Icons.Default.Settings,
            isSelected = currentRoute == "clothes_change" || currentRoute == "hairstyle_change",
            primaryColor = primaryColor,
            textSecondary = textSecondary,
            onClick = { navController.navigate("clothes_change") }
        )
        HtmlStyleNavItem(
            label = "Ghibli",
            icon = Icons.Default.Create,
            isSelected = currentRoute == "ghibli_art",
            primaryColor = primaryColor,
            textSecondary = textSecondary,
            onClick = { navController.navigate("ghibli_art") }
        )
        HtmlStyleNavItem(
            label = "Gallery",
            icon = ImageVector.vectorResource(id = R.drawable.ic_gallery),
            isSelected = currentRoute == "gallery",
            primaryColor = primaryColor,
            textSecondary = textSecondary,
            onClick = { navController.navigate("gallery") }
        )
    }
}

@Composable
fun HtmlStyleNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    primaryColor: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) primaryColor else textSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (isSelected) primaryColor else textSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun FuturisticBottomNavItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF00D4FF) else Color.White.copy(alpha = 0.6f),
        animationSpec = tween(300), label = "text"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "scale"
    )

    TextButton(onClick = onClick) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
        )
    }
}

@Composable
fun NeonGlassBottomNav(
    navController: NavController,
    currentRoute: String?,
    onShowGeneratePopup: () -> Unit = {}
) {
    // Match the app's signature color scheme
    val inactive = Color.White.copy(alpha = 0.7f)
    val active = Color.White
    val appCyan = Color(0xFF00B8FF)    // Main app cyan
    val appPurple = Color(0xFF8338EC)  // Main app purple
    val appTeal = Color(0xFF00F5D4)    // Main app teal

    Column(modifier = Modifier.fillMaxWidth()) {
        // Subtle gradient border matching app theme
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            appCyan.copy(alpha = 0.3f),
                            appPurple.copy(alpha = 0.4f),
                            appTeal.copy(alpha = 0.3f)
                        )
                    )
                )
        )
        
        // Glassmorphism navbar with app's signature gradient
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0D0D1A).copy(alpha = 0.85f), // Match app background
                            Color(0xFF1A1A2E).copy(alpha = 0.90f), // Slight variation
                            Color(0xFF0D0D1A).copy(alpha = 0.85f)
                        )
                    )
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            appCyan.copy(alpha = 0.08f),
                            appPurple.copy(alpha = 0.12f),
                            appTeal.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            NeonNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentRoute == "landing_page",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("landing_page") }
            )

            // Gallery
            NeonNavItem(
                icon = ImageVector.vectorResource(id = R.drawable.ic_gallery),
                label = "Gallery",
                selected = currentRoute == "gallery",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("gallery") }
            )

            // Generate (center highlighted)
            NeonCenterGenerateItem(
                selected = currentRoute == "hairstyle_change" || currentRoute == "ghibli_art",
                gradient = Brush.linearGradient(listOf(appCyan, appPurple, appTeal)),
                onClick = onShowGeneratePopup
            )

            // Profile button routes to Coins page
            NeonNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == "coins",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("coins") }
            )

            // Settings
            NeonNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                selected = currentRoute == "settings",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("settings") }
            )
        }
        
        // Bottom safe area with matching glassmorphism gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0D0D1A).copy(alpha = 0.85f),
                            Color(0xFF1A1A2E).copy(alpha = 0.90f),
                            Color(0xFF0D0D1A).copy(alpha = 0.85f)
                        )
                    )
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            appCyan.copy(alpha = 0.05f),
                            appPurple.copy(alpha = 0.08f),
                            appTeal.copy(alpha = 0.05f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun NeonNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(if (selected) activeColor else inactiveColor, label = "tint")
    TextButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, tint = tint)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, color = tint, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun NeonCenterGenerateItem(
    selected: Boolean,
    gradient: Brush,
    onClick: () -> Unit
) {
    // App signature colors for glow effects
    val appCyan = Color(0xFF00B8FF)
    val appPurple = Color(0xFF8338EC)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.offset(y = (-12).dp)
    ) {
        // Glowing circular button with app-themed glow
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(100),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(100),
                ambientColor = appCyan.copy(alpha = 0.6f),  // Cyan glow
                spotColor = appPurple.copy(alpha = 0.5f)    // Purple glow
            )
        ) {
            Box(
                modifier = Modifier
                    .background(gradient, shape = RoundedCornerShape(100))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Generate",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Generate",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun GenerateOptionsPopup(
    onDismiss: () -> Unit,
    onGenerateOutfit: () -> Unit,
    onGenerateHairstyle: () -> Unit,
    onBeardMaker: () -> Unit
) {
    // Backdrop
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Popup container with pointer
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 100.dp), // Space above the navbar
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Popup Card
            Card(
                modifier = Modifier
                    .width(240.dp)
                    .wrapContentHeight()
                    .clickable { }, // Prevent backdrop click from closing
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Generate",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Generate Outfit Option
                GenerateOptionItem(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_clothing),
                    title = "Generate Outfit",
                    subtitle = "Create a new outfit",
                    onClick = {
                        onGenerateOutfit()
                        onDismiss()
                    }
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Generate Hairstyle Option
                GenerateOptionItem(
                    icon = Icons.Default.AutoAwesome,
                    title = "Generate Hairstyle", 
                    subtitle = "Try new hairstyles",
                    onClick = {
                        onGenerateHairstyle()
                        onDismiss()
                    }
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Beard Maker Option
                GenerateOptionItem(
                    icon = Icons.Default.Person,
                    title = "Beard Maker",
                    subtitle = "Try different beard styles",
                    onClick = {
                        onBeardMaker()
                        onDismiss()
                    }
                )
            }
        }
            
            // Small pointer triangle pointing down to the generate button
            Canvas(
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = (-1).dp) // Slightly overlap with card
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(
                    path = path,
                    color = androidx.compose.ui.graphics.Color(0xFF1A1A2E)
                )
            }
        }
    }
}

@Composable
private fun GenerateOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A3E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00B8FF).copy(alpha = 0.2f),
                                Color(0xFF8338EC).copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF00B8FF),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text content
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
