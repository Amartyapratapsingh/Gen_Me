package com.example.genme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding

@Composable
fun CoinPurchasePage(navController: NavController) {
    val background = Color(0xFF111122)
    val cardBg = Color.White.copy(alpha = 0.05f)
    val borderColor = Color.White.copy(alpha = 0.10f)

    val primaryAccent = Color(0xFF00A9FF)
    val secondaryAccent = Color(0xFFA076F9)
    val tertiaryAccent = Color(0xFF00F5D4)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                )
        ) {
            // Header: back + centered title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable { navController.popBackStack() },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
                Text(
                    text = "Purchase Coins",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(44.dp))
            }

            // Title and subtitle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Get More Coins",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Fuel your creativity. More coins mean more stunning AI-generated designs.",
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Packages
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CoinPackageCard(
                    title = "100 Coins",
                    price = "$9.99",
                    primaryAccent = primaryAccent,
                    secondaryAccent = secondaryAccent,
                    tertiaryAccent = tertiaryAccent,
                    background = cardBg,
                    borderColor = borderColor,
                    onBuy = {}
                )

                // Popular
                Box {
                    CoinPackageCard(
                        title = "500 Coins",
                        price = "$44.99",
                        primaryAccent = primaryAccent,
                        secondaryAccent = secondaryAccent,
                        tertiaryAccent = tertiaryAccent,
                        background = cardBg,
                        borderColor = borderColor,
                        onBuy = {}
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(y = (-10).dp, x = 16.dp)
                            .clip(RoundedCornerShape(100))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(secondaryAccent, primaryAccent)
                                )
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "POPULAR",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                CoinPackageCard(
                    title = "1000 Coins",
                    price = "$79.99",
                    primaryAccent = primaryAccent,
                    secondaryAccent = secondaryAccent,
                    tertiaryAccent = tertiaryAccent,
                    background = cardBg,
                    borderColor = borderColor,
                    onBuy = {}
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "10 coins",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = primaryAccent
                    )
                    Text(
                        text = "per image generation",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Unleash your imagination without limits.",
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CoinPackageCard(
    title: String,
    price: String,
    primaryAccent: Color,
    secondaryAccent: Color,
    tertiaryAccent: Color,
    background: Color,
    borderColor: Color,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Diagonal decorative lines
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val strokeWidth = 2.dp.toPx()
                val lineColor = primaryAccent.copy(alpha = 0.3f)
                
                // Draw diagonal lines
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height * 0.3f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.6f, 0f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.4f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height * 0.4f),
                    strokeWidth = strokeWidth
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = price, color = Color(0xFFCBD5E1), fontSize = 16.sp)
                }

                // Simple blue button
                Button(
                    onClick = onBuy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryAccent
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "Buy Now",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

