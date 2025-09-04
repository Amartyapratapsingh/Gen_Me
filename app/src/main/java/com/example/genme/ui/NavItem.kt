package com.example.genme.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FuturisticNavItem(label: String, isActive: Boolean, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        // Futuristic icon with high visibility
        Card(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isActive) Color(0xFF161B22) else Color(0xFF0D1117)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isActive) 8.dp else 4.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (isActive) {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00D4FF).copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF374151).copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(18.dp)) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val strokeWidth = 2.dp.toPx()
                    val iconColor = if (isActive) Color(0xFF00F5FF) else Color(0xFF9CA3AF)

                    when (label) {
                        "Home" -> {
                            // Futuristic home icon
                            val path = Path().apply {
                                moveTo(centerX, centerY - 7.dp.toPx())
                                lineTo(centerX - 7.dp.toPx(), centerY + 1.dp.toPx())
                                lineTo(centerX - 5.dp.toPx(), centerY + 1.dp.toPx())
                                lineTo(centerX - 5.dp.toPx(), centerY + 7.dp.toPx())
                                lineTo(centerX + 5.dp.toPx(), centerY + 7.dp.toPx())
                                lineTo(centerX + 5.dp.toPx(), centerY + 1.dp.toPx())
                                lineTo(centerX + 7.dp.toPx(), centerY + 1.dp.toPx())
                                close()
                            }

                            if (isActive) {
                                drawPath(
                                    path = path,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF00D4FF).copy(alpha = 0.3f),
                                            Color(0xFF6366F1).copy(alpha = 0.2f)
                                        )
                                    )
                                )
                            }

                            drawPath(
                                path = path,
                                color = iconColor,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Door
                            drawRoundRect(
                                color = iconColor,
                                topLeft = Offset(centerX - 2.dp.toPx(), centerY + 3.dp.toPx()),
                                size = Size(4.dp.toPx(), 4.dp.toPx()),
                                cornerRadius = CornerRadius(1.dp.toPx()),
                                style = Stroke(width = strokeWidth)
                            )
                        }
                        "Gallery" -> {
                            // Futuristic gallery icon
                            if (isActive) {
                                drawRoundRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF00D4FF).copy(alpha = 0.3f),
                                            Color(0xFF8B5CF6).copy(alpha = 0.2f)
                                        )
                                    ),
                                    topLeft = Offset(centerX - 6.dp.toPx(), centerY - 4.dp.toPx()),
                                    size = Size(10.dp.toPx(), 6.dp.toPx()),
                                    cornerRadius = CornerRadius(1.5.dp.toPx())
                                )
                            }

                            drawRoundRect(
                                color = iconColor,
                                topLeft = Offset(centerX - 6.dp.toPx(), centerY - 4.dp.toPx()),
                                size = Size(10.dp.toPx(), 6.dp.toPx()),
                                cornerRadius = CornerRadius(1.5.dp.toPx()),
                                style = Stroke(width = strokeWidth)
                            )

                            drawRoundRect(
                                color = iconColor,
                                topLeft = Offset(centerX - 4.dp.toPx(), centerY - 1.dp.toPx()),
                                size = Size(10.dp.toPx(), 6.dp.toPx()),
                                cornerRadius = CornerRadius(1.5.dp.toPx()),
                                style = Stroke(width = strokeWidth)
                            )

                            // Tech accent dot
                            drawCircle(
                                color = if (isActive) Color(0xFF00F5FF) else iconColor,
                                radius = 1.5.dp.toPx(),
                                center = Offset(centerX - 2.dp.toPx(), centerY + 1.5.dp.toPx())
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color(0xFF00D4FF) else Color(0xFF9CA3AF),
            letterSpacing = 0.8.sp
        )

        // Futuristic active indicator
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF00F5FF),
                                Color(0xFF00D4FF).copy(alpha = 0.7f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFF00D4FF).copy(alpha = 0.6f),
                        spotColor = Color(0xFF00F5FF).copy(alpha = 0.8f)
                    )
            )
        } else {
            // Subtle inactive indicator
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = Color(0xFF6B7280).copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }
    }
}
