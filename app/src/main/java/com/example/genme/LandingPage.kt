package com.example.genme

import androidx.compose.animation.core.*
import androidx.compose.animation.animateColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genme.ui.FuturisticBottomNav
import kotlin.math.*
import kotlin.random.Random

@Composable
fun LandingPage(navController: NavController) {
    // Enhanced gradient animations
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val primaryGradient by infiniteTransition.animateColor(
        initialValue = Color(0xFF2D1B69), // Deep charcoal-purple
        targetValue = Color(0xFF5B21B6), // Plum
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "primary"
    )
    
    val secondaryGradient by infiniteTransition.animateColor(
        initialValue = Color(0xFF831843), // Deep plum
        targetValue = Color(0xFF1E293B), // Dark charcoal
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "secondary"
    )
    
    // Floating particles and bokeh effects
    val particleFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "particles"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1F2937), // Dark charcoal
                        Color(0xFF111827), // Deeper charcoal
                        Color(0xFF000000)  // Pure black
                    ),
                    center = Offset(0.5f, 0.3f)
                )
            )
    ) {
        // Enhanced background with bokeh and prism effects
        EnhancedBackground(primaryGradient, secondaryGradient)
        
        // Colorful bokeh particles
        ColorfulBokeh(
            modifier = Modifier.fillMaxSize(),
            animationProgress = particleFloat
        )
        
        // Prism flares in corners
        PrismFlares(modifier = Modifier.fillMaxSize())
        
        // Main content layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Futuristic header with high visibility
            FuturisticHeader()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Three equal-sized feature cards in horizontal row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FuturisticFeatureCard(
                    title = "Clothes",
                    subtitle = "Changer",
                    iconType = "tshirt",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("clothes_change") },
                    isComingSoon = false
                )
                
                FuturisticFeatureCard(
                    title = "Hairstyle",
                    subtitle = "Changer",
                    iconType = "hair",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("hairstyle_change") },
                    isComingSoon = false
                )
                
                FuturisticFeatureCard(
                    title = "Ghibli Art",
                    subtitle = "Maker",
                    iconType = "art",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("ghibli_art") },
                    isComingSoon = false
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Scroll indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(4.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Futuristic bottom navigation
            FuturisticBottomNav(navController = navController)
            
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun EnhancedBackground(
    primaryColor: Color,
    secondaryColor: Color
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Deep charcoal to plum gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.7f),
                            secondaryColor.copy(alpha = 0.5f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1500f)
                    )
                )
        )
        
        // Additional gradient overlay for depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4C1D95).copy(alpha = 0.3f), // Purple
                            Color(0xFF831843).copy(alpha = 0.2f), // Plum
                            Color.Transparent
                        ),
                        center = Offset(0.7f, 0.2f),
                        radius = 800f
                    )
                )
        )
    }
}

@Composable
fun ColorfulBokeh(
    modifier: Modifier = Modifier,
    animationProgress: Float
) {
    Canvas(modifier = modifier) {
        // Soft colorful bokeh particles
        repeat(15) { index ->
            val baseX = (index * 120f) % size.width
            val baseY = (index * 150f) % size.height
            
            val x = baseX + sin((animationProgress * 2 * PI + index * 0.5).toDouble()).toFloat() * 40f
            val y = baseY + cos((animationProgress * 1.5 * PI + index * 0.7).toDouble()).toFloat() * 50f
            
            val alpha = (sin((animationProgress * PI + index * 0.3).toDouble()).toFloat() * 0.4f + 0.2f).coerceIn(0f, 0.6f)
            
            val colors = listOf(
                Color(0xFFA78BFA), // Lavender
                Color(0xFF67E8F9), // Cyan
                Color(0xFFFBBF24), // Peach/yellow
                Color(0xFFF472B6), // Pink
                Color(0xFF86EFAC)  // Mint
            )
            val color = colors[index % colors.size]
            
            // Large soft glowing circles
            drawCircle(
                color = color.copy(alpha = alpha * 0.3f),
                radius = 25f,
                center = Offset(x % size.width, y % size.height)
            )
            
            // Smaller bright center
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = 3f,
                center = Offset(x % size.width, y % size.height)
            )
        }
    }
}

@Composable
fun PrismFlares(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // Corner prism flares
        val flareColors = listOf(
            Color(0xFFA78BFA).copy(alpha = 0.1f), // Lavender
            Color(0xFF67E8F9).copy(alpha = 0.1f), // Cyan
            Color(0xFFFBBF24).copy(alpha = 0.1f), // Peach
        )
        
        // Top-left corner flare
        drawArc(
            brush = Brush.radialGradient(
                colors = flareColors,
                center = Offset(0f, 0f),
                radius = 200f
            ),
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(-100f, -100f),
            size = Size(300f, 300f)
        )
        
        // Bottom-right corner flare
        drawArc(
            brush = Brush.radialGradient(
                colors = flareColors.reversed(),
                center = Offset(size.width, size.height),
                radius = 200f
            ),
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(size.width - 200f, size.height - 200f),
            size = Size(300f, 300f)
        )
    }
}

@Composable
fun FuturisticHeader() {
    // Futuristic pulsing effect
    val infiniteTransition = rememberInfiniteTransition(label = "futuristic_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )
    
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "border_glow"
    )
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F0F23) // Dark futuristic background
        ),
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00D4FF).copy(alpha = borderGlow), // Cyan
                    Color(0xFF6366F1).copy(alpha = borderGlow), // Blue
                    Color(0xFF8B5CF6).copy(alpha = borderGlow), // Purple
                    Color(0xFF00D4FF).copy(alpha = borderGlow)  // Cyan
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0xFF00D4FF).copy(alpha = 0.3f),
                spotColor = Color(0xFF6366F1).copy(alpha = 0.2f)
            )
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                            Color(0xFF1A1A2E), // Dark blue
                            Color(0xFF16213E), // Darker blue
                            Color(0xFF0F1419)  // Almost black
                            ),
                            start = Offset(0f, 0f),
                        end = Offset(0f, 500f)
                        )
                    )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // "Virtual" with futuristic cyan gradient
                Text(
                    text = "Virtual",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W300,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00F5FF), // Bright cyan
                                Color(0xFF00D4FF), // Cyan
                                Color(0xFF0891B2)  // Darker cyan
                            )
                        )
                    ),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(pulseAlpha)
                )
                
                // "Style" with futuristic gold gradient
                Text(
                    text = "Style",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFFBEB), // Very light yellow
                                Color(0xFFFBBF24), // Golden yellow
                                Color(0xFFEAB308)  // Darker gold
                            )
                        )
                    ),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
                
                // "Studio" with futuristic purple gradient
                Text(
                    text = "Studio",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE0E7FF), // Light purple
                                Color(0xFF8B5CF6), // Purple
                                Color(0xFF6D28D9)  // Darker purple
                            )
                        )
                    ),
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Futuristic subtitle with better visibility
                Text(
                    text = "AI • POWERED • TRANSFORMATION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF00D4FF).copy(alpha = 0.9f),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FuturisticFeatureCard(
    title: String,
    subtitle: String,
    iconType: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isComingSoon: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )
    
    // Futuristic border glow animation
    val borderGlow by rememberInfiniteTransition(label = "card_glow").animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )
    
    Card(
        modifier = modifier
            .aspectRatio(0.75f)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = if (!isComingSoon) onClick else { {} }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1117) // Dark futuristic background
        ),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00D4FF).copy(alpha = borderGlow * 0.7f), // Cyan
                    Color(0xFF6366F1).copy(alpha = borderGlow * 0.8f), // Blue
                    Color(0xFF8B5CF6).copy(alpha = borderGlow * 0.6f)  // Purple
                )
            )
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF161B22), // Dark gray
                            Color(0xFF0D1117), // Darker
                            Color(0xFF010409)  // Almost black
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                FuturisticIcon(iconType = iconType, size = 48.dp)
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isComingSoon) Color.White.copy(alpha = 0.7f) else Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isComingSoon) 
                            Color(0xFF6B7280) 
                        else 
                            Color(0xFF00D4FF).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Futuristic coming soon indicator
            if (isComingSoon) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF374151)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF6B7280)
                    )
                ) {
                    Text(
                        text = "SOON",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FuturisticIcon(iconType: String, size: androidx.compose.ui.unit.Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val centerX = this.size.width / 2
        val centerY = this.size.height / 2
        val strokeWidth = 2.dp.toPx()
        
        when (iconType) {
            "tshirt" -> {
                // Futuristic T-shirt with high visibility
                val path = Path().apply {
                    // T-shirt body
                    moveTo(centerX - 18.dp.toPx(), centerY - 6.dp.toPx())
                    lineTo(centerX + 18.dp.toPx(), centerY - 6.dp.toPx())
                    lineTo(centerX + 15.dp.toPx(), centerY + 18.dp.toPx())
                    lineTo(centerX - 15.dp.toPx(), centerY + 18.dp.toPx())
                    close()
                }
                
                // Futuristic gradient fill
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF00D4FF).copy(alpha = 0.3f), // Cyan
                            Color(0xFF6366F1).copy(alpha = 0.2f)  // Blue
                        )
                    )
                )
                
                // Bright outline
                drawPath(
                    path = path,
                    color = Color(0xFF00F5FF), // Bright cyan
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                
                // Futuristic collar/neck
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - 6.dp.toPx(), centerY - 6.dp.toPx())
                        quadraticBezierTo(centerX, centerY - 1.dp.toPx(), centerX + 6.dp.toPx(), centerY - 6.dp.toPx())
                    },
                    color = Color(0xFF00F5FF),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                
                // Tech accent lines
                drawLine(
                    color = Color(0xFF6366F1),
                    start = Offset(centerX - 8.dp.toPx(), centerY + 8.dp.toPx()),
                    end = Offset(centerX + 8.dp.toPx(), centerY + 8.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            
            "hair" -> {
                // Futuristic hair design
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.3f), // Purple
                            Color(0xFF6366F1).copy(alpha = 0.2f)  // Blue
                        )
                    ),
                    radius = 14.dp.toPx(),
                    center = Offset(centerX, centerY + 3.dp.toPx())
                )
                
                drawCircle(
                    color = Color(0xFFE0E7FF), // Light purple
                    radius = 14.dp.toPx(),
                    center = Offset(centerX, centerY + 3.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
                
                // Futuristic hair outline
                val hairPath = Path().apply {
                    moveTo(centerX - 16.dp.toPx(), centerY - 8.dp.toPx())
                    quadraticBezierTo(centerX - 8.dp.toPx(), centerY - 18.dp.toPx(), centerX, centerY - 15.dp.toPx())
                    quadraticBezierTo(centerX + 8.dp.toPx(), centerY - 18.dp.toPx(), centerX + 16.dp.toPx(), centerY - 8.dp.toPx())
                    quadraticBezierTo(centerX + 12.dp.toPx(), centerY - 2.dp.toPx(), centerX + 8.dp.toPx(), centerY + 4.dp.toPx())
                    quadraticBezierTo(centerX, centerY + 6.dp.toPx(), centerX - 8.dp.toPx(), centerY + 4.dp.toPx())
                    quadraticBezierTo(centerX - 12.dp.toPx(), centerY - 2.dp.toPx(), centerX - 16.dp.toPx(), centerY - 8.dp.toPx())
                }
                
                drawPath(
                    path = hairPath,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.4f),
                            Color(0xFF6366F1).copy(alpha = 0.3f)
                        )
                    )
                )
                
                drawPath(
                    path = hairPath,
                    color = Color(0xFFE0E7FF),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            
            "art" -> {
                // Futuristic art palette
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF00D4FF), // Cyan
                            Color(0xFF6366F1), // Blue
                            Color(0xFF8B5CF6), // Purple
                            Color(0xFFFBBF24)  // Yellow
                        ),
                        start = Offset(centerX - 12.dp.toPx(), centerY - 12.dp.toPx()),
                        end = Offset(centerX + 12.dp.toPx(), centerY + 12.dp.toPx())
                    ),
                    topLeft = Offset(centerX - 12.dp.toPx(), centerY - 12.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 24.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                )
                
                // Futuristic border
                drawRoundRect(
                    color = Color(0xFF00F5FF),
                    topLeft = Offset(centerX - 12.dp.toPx(), centerY - 12.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 24.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
                
                // Tech accent dot
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = Offset(centerX + 6.dp.toPx(), centerY - 6.dp.toPx())
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF1A1A2E,
    showSystemUi = true
)
@Composable
fun LandingPagePreview() {
    MaterialTheme {
        LandingPage(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleTestPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E),
                            Color(0xFF0F1419)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFFEC4899),
                            Color(0xFF06B6D4)
                        )
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Virtual",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                    Text(
                        text = "Style",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Studio",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "Appearance Changer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}