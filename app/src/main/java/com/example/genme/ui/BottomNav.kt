package com.example.genme.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
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
