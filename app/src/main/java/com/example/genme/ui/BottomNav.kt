package com.example.genme.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.genme.R

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
    currentRoute: String?
) {
    val inactive = Color.White.copy(alpha = 0.6f)
    val active = Color.White
    val cyan = Color(0xFF22D3EE)
    val purple = Color(0xFF7C3AED)

    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
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

            // Styles
            NeonNavItem(
                icon = ImageVector.vectorResource(id = R.drawable.ic_clothing),
                label = "Styles",
                selected = currentRoute == "clothes_change",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("clothes_change") }
            )

            // Generate (center highlighted)
            NeonCenterGenerateItem(
                selected = currentRoute == "hairstyle_change" || currentRoute == "ghibli_art",
                gradient = Brush.linearGradient(listOf(cyan, purple)),
                onClick = { /* Keep on current generation page */ }
            )

            // Profile
            NeonNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == "profile",
                activeColor = active,
                inactiveColor = inactive,
                onClick = { navController.navigate("profile") }
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
        // Bottom safe area tint
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Black.copy(alpha = 0.3f))
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.offset(y = (-12).dp)
    ) {
        // Glowing circular button
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(100),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(gradient, shape = RoundedCornerShape(100))
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(100),
                        ambientColor = Color(0x8800F6FF),
                        spotColor = Color(0x887D32FF)
                    )
                    .padding(12.dp)
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
            color = if (selected) Color.White else Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
