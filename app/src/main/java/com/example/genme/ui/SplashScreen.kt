package com.example.genme.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.border
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val richBlack = Color(0xFF0A0510)
    val blue = Color(0xFF00BFFF)
    val purple = Color(0xFF9400D3)
    val cyan = Color(0xFF00FFFF)

    LaunchedEffect(Unit) {
        delay(1800)
        navController.navigate("landing_page") {
            popUpTo("splash") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(richBlack)
    ) {
        // Ambient gradient glows
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(blue.copy(alpha = 0.18f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(200f, 300f),
                        radius = 600f
                    )
                )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(purple.copy(alpha = 0.16f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(900f, 1600f),
                        radius = 900f
                    )
                )
        )

        // Data streams (animated vertical glows)
        DataStream(xFraction = 0.10f, color = blue, durationMs = 10000, delayMs = -2000)
        DataStream(xFraction = 0.25f, color = purple, durationMs = 8000, delayMs = -5000, opacity = 0.2f)
        DataStream(xFraction = 0.40f, color = cyan, durationMs = 12000, delayMs = -1000, opacity = 0.35f)
        DataStream(xFraction = 0.60f, color = blue, durationMs = 9000, delayMs = -8000, opacity = 0.25f)
        DataStream(xFraction = 0.75f, color = purple, durationMs = 7000, delayMs = -3000, opacity = 0.30f)
        DataStream(xFraction = 0.90f, color = cyan, durationMs = 11000, delayMs = -6000, opacity = 0.22f)

        // Tessellation-like subtle shapes
        TessellationTriangle(
            top = 0.15f, left = 0.20f,
            color = blue.copy(alpha = 0.06f), triSize = 70.dp,
            durationMs = 12000, rotateStart = -10f, rotateEnd = 10f, opacityStart = 0.05f, opacityEnd = 0.10f
        )
        TessellationTriangle(
            top = 0.30f, left = 0.70f,
            color = purple.copy(alpha = 0.05f), triSize = 60.dp,
            durationMs = 10000, rotateStart = -30f, rotateEnd = 0f, opacityStart = 0.08f, opacityEnd = 0.04f
        )
        TessellationTriangle(
            top = 0.55f, left = 0.08f,
            color = cyan.copy(alpha = 0.04f), triSize = 56.dp,
            durationMs = 18000, rotateStart = 5f, rotateEnd = 25f, opacityStart = 0.03f, opacityEnd = 0.08f
        )

        // Center content
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Glass logo ring
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .shadow(20.dp, shape = CircleShape, ambientColor = blue.copy(0.25f), spotColor = purple.copy(0.25f))
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f)),
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.10f))
                ) { Box(Modifier.fillMaxSize()) }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.02f))
                )

                Box(
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = com.example.genme.R.drawable.logo_circle_clean),
                        contentDescription = "Gen ME Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(170.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Title: Gen ME with animated gradient on ME
            AnimatedBrandTitle()

            Spacer(Modifier.height(12.dp))

            // Loading subtle fade
            SubtleLoading()
        }
    }
}

@Composable
private fun DataStream(
    xFraction: Float,
    color: Color,
    durationMs: Int,
    delayMs: Int = 0,
    opacity: Float = 0.3f
) {
    val transition = rememberInfiniteTransition(label = "stream")
    val shift by transition.animateFloat(
        initialValue = -900f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, delayMillis = maxOf(0, -delayMs), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shift"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .offset(x = (xFraction * 360).dp, y = shift.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, color, Color.Transparent)))
                .align(Alignment.TopStart)
                .alpha(opacity)
        )
    }
}

@Composable
private fun TessellationTriangle(
    top: Float,
    left: Float,
    color: Color,
    triSize: androidx.compose.ui.unit.Dp,
    durationMs: Int,
    rotateStart: Float,
    rotateEnd: Float,
    opacityStart: Float,
    opacityEnd: Float
) {
    val transition = rememberInfiniteTransition(label = "tess")
    val rot by transition.animateFloat(
        initialValue = rotateStart,
        targetValue = rotateEnd,
        animationSpec = infiniteRepeatable(tween(durationMs, easing = LinearEasing), RepeatMode.Reverse),
        label = "rot"
    )
    val alpha by transition.animateFloat(
        initialValue = opacityStart,
        targetValue = opacityEnd,
        animationSpec = infiniteRepeatable(tween(durationMs, easing = LinearEasing), RepeatMode.Reverse),
        label = "alpha"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(triSize)
                .offset(x = (left * 360).dp, y = (top * 720).dp)
                .rotate(rot)
                .alpha(alpha)
        ) {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path = path, color = color, style = androidx.compose.ui.graphics.drawscope.Fill)
        }
    }
}

@Composable
private fun AnimatedBrandTitle() {
    val blue = Color(0xFF00BFFF)
    val purple = Color(0xFF9400D3)
    val cyan = Color(0xFF00FFFF)
    val transition = rememberInfiniteTransition(label = "grad")
    val shift by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "shift"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Gen ", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
            val grad = Brush.linearGradient(
                colors = listOf(blue, purple, cyan),
                start = androidx.compose.ui.geometry.Offset(0f + shift * 400f, 0f),
                end = androidx.compose.ui.geometry.Offset(400f + shift * 400f, 0f)
            )
            Text(
                text = "ME",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Transparent,
                style = androidx.compose.ui.text.TextStyle(brush = grad)
            )
        }
    }
}

@Composable
private fun SubtleLoading() {
    val transition = rememberInfiniteTransition(label = "load")
    val alpha by transition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "alpha"
    )
    Text(text = "LOADING", color = Color(0xFF9CA3AF).copy(alpha = alpha), fontSize = 12.sp, fontWeight = FontWeight.Light)
}
