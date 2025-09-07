package com.example.genme

import androidx.compose.animation.core.*
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genme.ui.FuturisticBottomNav
import com.example.genme.ui.HtmlStyleBottomNav
import com.example.genme.R
import coil.compose.AsyncImage
import kotlin.math.*
import kotlin.random.Random

@Composable
fun LandingPage(navController: NavController) {
    val bg = Color(0xFF0D0D1A)
    val borderBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF00B8FF), Color(0xFF8338EC), Color(0xFF00F5D4))
    )

    Box(modifier = Modifier.fillMaxSize().background(bg)) {
        // Soft top glow
    Box(
        modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
            .background(
                    Brush.linearGradient(
                        listOf(Color(0x3300B8FF), Color(0x338338EC), Color(0x3300F5D4))
                    )
                )
                .align(Alignment.TopCenter)
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
            ) {
                TopHeader(onCoins = { navController.navigate("coins") })
                SearchBar()
                FeatureCarousel(
                    onOutfits = { navController.navigate("clothes_change") },
                    onHairstyle = { navController.navigate("hairstyle_change") },
                    onBeardMaker = { navController.navigate("beard_maker") },
                    onAgeChanger = { navController.navigate("age_changer") },
                    borderBrush = borderBrush
                )
                Text(
                    text = "Tools",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                ToolsGrid(
                    borderBrush = borderBrush,
                    onCloth = { navController.navigate("clothes_change") },
                    onHair = { navController.navigate("hairstyle_change") },
                    onBackground = { /* disabled: no navigation */ },
                    onAvatar = { navController.navigate("beard_maker") },
                    onFilters = { navController.navigate("age_changer") },
                    onFaceGen = { /* disabled: no navigation */ }
                )
                Spacer(Modifier.height(12.dp))
            }
            // Bottom nav provided by MainActivity Scaffold
        }
    }
}

@Composable
private fun TopHeader(onCoins: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.width(48.dp))
        Text(
            text = "Gen ME",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )
        
        // Coin Display
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clickable { onCoins() }
                .padding(8.dp)
        ) {
            // Coin Icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
                        ),
                        shape = CircleShape
                    )
                    .border(1.dp, Color(0xFFFF8C00), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Coin Count
            Text(
                text = "9999",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Add Icon
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add coins",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar() {
    val query = remember { mutableStateOf("") }
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
            singleLine = true,
            placeholder = { Text("Search", color = Color.White.copy(0.5f)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.White.copy(0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF8338EC),
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0x991A1A2E),
                unfocusedContainerColor = Color(0x991A1A2E),
                cursorColor = Color(0xFF8338EC)
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun FeatureCarousel(
    onOutfits: () -> Unit,
    onHairstyle: () -> Unit,
    onBeardMaker: () -> Unit,
    onAgeChanger: () -> Unit,
    borderBrush: Brush
) {
    val cardShape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
        FeatureCard(
            imageRes = R.drawable.cloth_changer_banner,
            label = "Try New Outfits",
            onClick = onOutfits,
            shape = cardShape,
            borderBrush = borderBrush
        )
        FeatureCard(
            imageRes = R.drawable.hairstyle_changer_banner,
            label = "Change Hairstyle",
            onClick = onHairstyle,
            shape = cardShape,
            borderBrush = borderBrush
        )
        FeatureCard(
            imageRes = R.drawable.beard_maker_banner,
            label = "Beard Maker",
            onClick = onBeardMaker,
            shape = cardShape,
            borderBrush = borderBrush
        )
        FeatureCard(
            imageRes = R.drawable.age_changer_banner,
            label = "Age Changer",
            onClick = onAgeChanger,
            shape = cardShape,
            borderBrush = borderBrush
        )
    }
}

@Composable
private fun FeatureCard(
    imageUrl: String? = null,
    imageRes: Int? = null,
    label: String,
    onClick: () -> Unit,
    shape: RoundedCornerShape,
    borderBrush: Brush
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            onClick = onClick,
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .width(220.dp)
                .aspectRatio(4f / 5f)
                .border(width = 1.dp, brush = borderBrush, shape = shape)
                .clip(shape)
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(text = label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ToolsGrid(
    borderBrush: Brush,
    onCloth: () -> Unit,
    onHair: () -> Unit,
    onBackground: () -> Unit,
    onAvatar: () -> Unit,
    onFilters: () -> Unit,
    onFaceGen: () -> Unit
) {
    val items = listOf(
        ToolItem("Cloth Changer", null, R.drawable.cloth_changer_banner, onCloth),
        ToolItem("Hairstyle Changer", null, R.drawable.hairstyle_changer_banner, onHair),
        ToolItem("Background Changer", "https://lh3.googleusercontent.com/aida-public/AB6AXuBPjm2PnlRLSMgFsUUMJhmEn7KIPjJNulqvxf5ZA0_qaeNBJlU7Xons8vg7jNpH7pWUaHt5L841wDoQkbZsyhsNY3Hnz2JHxs14MmesDZwjRKmg6i3CW96q9cc89P48O3t4_5UUgruVWewZrfu8DaRHDI627kYJLed9QSxtxEail2sGwcPd10Be_2bp3wbU_VNjFTdyRVihB7j6YIUs3bF55y2C1m97ULfSvHfVCB2RZtGhPlfiPmbI8Kzf260ADEuANC4Q9a4O4no", null, onBackground),
        ToolItem("Beard Maker", null, R.drawable.beard_maker_banner, onAvatar),
        ToolItem("Age Changer", null, R.drawable.age_changer_banner, onFilters),
        ToolItem("Face Generator", "https://lh3.googleusercontent.com/aida-public/AB6AXuCtjuaG_csFUAI4u4HEZhOSAv1ks69jxaxSO_IpPh9hhJ2k8CablgNv6_OjEkjr9HNKZ2tbYoyA2rdSmg0xA9P_-PUcq9JmMYNZ2fqZ8Qe0Vp3vUXG1a0UzZC8LsrxqdcecZsAiphg9yZiftWuJzDSRLAdPpIaiV9IVTEckY-cl208SZb_2y08-jN_gjMHXptFaLk0rxevDFkjrpYDJ6nBg0f3MXgeiuKYriSjescJPh3F6wrovkfA3p9E4mleLXqqVhcgEmFIvkSc", null, onFaceGen)
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        for (row in items.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { item ->
                    ToolCard(item = item, borderBrush = borderBrush, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

private data class ToolItem(val title: String, val imageUrl: String? = null, val imageRes: Int? = null, val onClick: () -> Unit)

@Composable
private fun ToolCard(item: ToolItem, borderBrush: Brush, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(Color(0x991A1A2E))
            .border(width = 1.dp, brush = borderBrush, shape = shape)
            .clickable { item.onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (item.imageRes != null) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else if (item.imageUrl != null) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = item.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
private fun FooterNav(
    onHome: () -> Unit,
    onStyles: () -> Unit,
    onGenerate: () -> Unit,
    onProfile: () -> Unit
) {
    val pillBrush = Brush.linearGradient(listOf(Color(0xFF00B8FF), Color(0xFF8338EC)))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xCC0D0D1A))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterItemSelected(label = "Home", brush = pillBrush, onClick = onHome)
        FooterItem(iconRes = R.drawable.ic_clothing, label = "Styles", onClick = onStyles)
        FooterItem(iconRes = R.drawable.ic_gibli, label = "Generate", onClick = onGenerate)
        FooterItem(iconRes = R.drawable.ic_person, label = "Profile", onClick = onProfile)
    }
}

@Composable
private fun FooterItem(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = iconRes), contentDescription = label, tint = Color.White.copy(0.6f), modifier = Modifier.size(24.dp))
        Text(text = label, color = Color.White.copy(0.6f), fontSize = 12.sp)
    }
}

@Composable
private fun FooterItemSelected(label: String, brush: Brush, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 32.dp)
                .clip(RoundedCornerShape(100))
                .background(brush)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.Home, contentDescription = label, tint = Color.White)
        }
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun PremiumHeroSection(
    primaryColor: Color,
    accentColor: Color,
    textSecondary: Color,
    onTryClothes: () -> Unit,
    onTryHair: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x66282828)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0x33FFFFFF),
                            Color.Transparent
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "GenMe",
                    fontSize = 14.sp,
                    color = textSecondary
                )
                Text(
                    text = "Virtual Style Studio",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(colors = listOf(primaryColor, accentColor))
                    )
                )
                Text(
                    text = "AI‑powered try‑ons. New looks in seconds.",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onTryClothes,
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(20.dp)
                    ) { Text("Try Clothes", color = Color.Black, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = onTryHair,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor)
                    ) { Text("Change Hairstyle") }
                }
            }
        }
    }
}

@Composable
fun PremiumFeatureGrid(
    primaryColor: Color,
    textSecondary: Color,
    onClothes: () -> Unit,
    onHair: () -> Unit,
    onArt: () -> Unit,
    onGallery: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            PremiumFeatureCard(
                title = "Clothes",
                subtitle = "Virtual try‑on",
                icon = Icons.Default.CheckCircle,
                primaryColor = primaryColor,
                onClick = onClothes,
                modifier = Modifier.weight(1f)
            )
            PremiumFeatureCard(
                title = "Hairstyle",
                subtitle = "Find your cut",
                icon = Icons.Default.Face,
                primaryColor = primaryColor,
                onClick = onHair,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            PremiumFeatureCard(
                title = "Ghibli Art",
                subtitle = "Stylize portraits",
                icon = Icons.Default.Create,
                primaryColor = primaryColor,
                onClick = onArt,
                modifier = Modifier.weight(1f)
            )
            PremiumFeatureCard(
                title = "Gallery",
                subtitle = "Saved results",
                icon = Icons.Default.Favorite,
                primaryColor = primaryColor,
                onClick = onGallery,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PremiumFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "card_scale")

    Card(
        modifier = modifier
            .height(120.dp)
            .graphicsLayer {
                scaleX = scale; scaleY = scale
            }
            .clickable(interactionSource = interaction, indication = null) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x66282828)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
            brush = Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(icon, contentDescription = title, tint = primaryColor, modifier = Modifier.size(24.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color(0xFFB3B3B3), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun HtmlStyleHeader(
    primaryColor: Color,
    accentColor: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp), // NO HORIZONTAL PADDING - FULL WIDTH
        shape = RoundedCornerShape(
            topStart = 0.dp, 
            topEnd = 0.dp, 
            bottomStart = 24.dp, 
            bottomEnd = 24.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80282828) // glassmorphic-card background
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
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
                .padding(top = 60.dp, bottom = 40.dp, start = 16.dp, end = 16.dp), // Reduced padding
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                        Text(
                    text = "Virtual Style Studio",
                    fontSize = 48.sp, // text-5xl
                    fontWeight = FontWeight.Black, // font-black
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(primaryColor, accentColor)
                        )
                    ),
                            textAlign = TextAlign.Center,
                    letterSpacing = (-2).sp // tracking-tighter
                )
                
                Text(
                    text = "Appearance Changer",
                    fontSize = 20.sp, // text-xl
                    fontWeight = FontWeight.Medium,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HtmlStyleFeatureCard(
    title: String,
    subtitle: String,
    buttonText: String,
    iconType: String,
    primaryColor: Color,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp), // rounded-2xl
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // min-height: 140px from HTML
        ) {
            // Background image based on card type
            when (iconType) {
                "shirt" -> {
                    // Use actual Unsplash image for clothes changer
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1579664531470-ac357f8f8e2b?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        contentDescription = "Fashion clothing background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.4f
                    )
                }
                "scissors" -> {
                    // Use local image for hairstyle changer
                    Image(
                        painter = painterResource(id = R.drawable.hairstyle_background),
                        contentDescription = "Hairstyle background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.4f
                    )
                }
                else -> {
                    // Use custom drawn background for other cards
                    BackgroundImageForCard(iconType = iconType)
                }
            }
            
            // Colored gradient overlay for each card type
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = when (iconType) {
                            "shirt" -> Brush.radialGradient(
                colors = listOf(
                                    Color(0xFF4A5568).copy(alpha = 0.6f), // Gray for clothes
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                radius = 300f
                            )
                            "scissors" -> Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF805AD5).copy(alpha = 0.5f), // Purple for hair
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                radius = 300f
                            )
                            "wand" -> Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4299E1).copy(alpha = 0.5f), // Blue for art
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                radius = 300f
                            )
                            else -> Brush.radialGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
            )
            
            // Glassmorphic backdrop blur effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                                Color(0x4D000000),
                                Color(0x66000000)
                            )
                        )
                    )
            )
            
            // Content layout exactly like HTML
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // p-4
            ) {
                // Top section with icon and text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp), // gap-3
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    // Icon container
                    Box(
                        modifier = Modifier
                            .size(48.dp) // h-12 w-12
                            .background(
                                color = primaryColor.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        HtmlStyleIcon(iconType = iconType, primaryColor = primaryColor)
                    }
                
                Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = title,
                            fontSize = 18.sp, // text-lg
                            fontWeight = FontWeight.Bold, // font-bold
                            color = Color.White
                    )
                    Text(
                        text = subtitle,
                            fontSize = 14.sp, // text-sm
                        fontWeight = FontWeight.Medium,
                            color = Color(0xFFB3B3B3) // text-gray-300
                        )
                    }
                }
                
                // Bottom right button
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(20.dp), // rounded-full
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .height(32.dp), // py-2
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // px-4 py-2
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 12.sp, // text-xs
                        fontWeight = FontWeight.Bold, // font-bold
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundImageForCard(iconType: String) {
    // Create realistic background images for each card type
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height
        
        when (iconType) {
            "shirt" -> {
                // Enhanced Clothes/Fashion background
                
                // Fashion hangers
                repeat(3) { i ->
                    val x = width * (0.2f + i * 0.3f)
                    val y = height * 0.15f
                    
                    // Hanger hook
                    drawLine(
                        color = Color.White.copy(alpha = 0.4f),
                        start = Offset(x, y - 10f),
                        end = Offset(x, y + 5f),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                    
                    // Hanger body
                    val hangerPath = Path().apply {
                        moveTo(x - 20f, y)
                        quadraticBezierTo(x - 25f, y + 8f, x - 15f, y + 12f)
                        lineTo(x + 15f, y + 12f)
                        quadraticBezierTo(x + 25f, y + 8f, x + 20f, y)
                    }
                    
                drawPath(
                        path = hangerPath,
                        color = Color.White.copy(alpha = 0.3f),
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }
                
                // Clothing items hanging
                repeat(3) { i ->
                    val x = width * (0.2f + i * 0.3f)
                    val y = height * 0.25f
                    
                    // Shirt silhouette hanging from hanger
                    val clothingPath = Path().apply {
                        moveTo(x - 18f, y)
                        lineTo(x - 22f, y + 15f) // shoulder
                        lineTo(x - 15f, y + 45f) // side
                        lineTo(x + 15f, y + 45f) // bottom
                        lineTo(x + 22f, y + 15f) // other shoulder
                        lineTo(x + 18f, y)
                        // neckline
                        quadraticBezierTo(x, y + 8f, x - 18f, y)
                    }
                    
                drawPath(
                        path = clothingPath,
                        color = Color.White.copy(alpha = 0.2f + i * 0.1f)
                    )
                    
                    // Add clothing details
                drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = Offset(x - 10f, y + 25f),
                        end = Offset(x + 10f, y + 25f),
                        strokeWidth = 1f
                    )
                }
                
                // Fashion accessories scattered
                repeat(6) { i ->
                    val x = (i * 67 + 30) % width.toInt().toFloat()
                    val y = height * 0.75f + (i % 3 - 1) * 15f
                    
                    when (i % 3) {
                        0 -> {
                            // Button
                drawCircle(
                                color = Color.White.copy(alpha = 0.2f),
                                radius = 3f,
                                center = Offset(x, y)
                            )
                        }
                        1 -> {
                            // Zipper teeth
                            repeat(3) { z ->
                                drawRect(
                                    color = Color.White.copy(alpha = 0.15f),
                                    topLeft = Offset(x + z * 4f, y - 6f),
                                    size = Size(2f, 3f)
                                )
                            }
                        }
                        2 -> {
                            // Thread spool
                            drawOval(
                                color = Color.White.copy(alpha = 0.18f),
                                topLeft = Offset(x - 4f, y - 6f),
                                size = Size(8f, 12f)
                            )
                        }
                    }
                }
                
                // Fabric texture pattern
                repeat(15) { i ->
                    val x1 = (i * 23) % width.toInt().toFloat()
                    val y1 = (i * 31) % height.toInt().toFloat()
                
                drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        radius = 1f,
                        center = Offset(x1, y1)
                    )
                }
            }
            
            "scissors" -> {
                // Enhanced Hair/Beauty background
                
                // Professional hair salon background
                // Hair cutting scissors
                val scissorsX = width * 0.15f
                val scissorsY = height * 0.3f
                
                // Scissors blade 1
                val blade1 = Path().apply {
                    moveTo(scissorsX, scissorsY)
                    lineTo(scissorsX + 25f, scissorsY - 5f)
                    lineTo(scissorsX + 30f, scissorsY + 2f)
                    lineTo(scissorsX + 5f, scissorsY + 7f)
                    close()
                }
                
                drawPath(
                    path = blade1,
                    color = Color.White.copy(alpha = 0.3f)
                )
                
                // Scissors blade 2
                val blade2 = Path().apply {
                    moveTo(scissorsX, scissorsY)
                    lineTo(scissorsX + 25f, scissorsY + 5f)
                    lineTo(scissorsX + 30f, scissorsY - 2f)
                    lineTo(scissorsX + 5f, scissorsY - 7f)
                    close()
                }
                
                drawPath(
                    path = blade2,
                    color = Color.White.copy(alpha = 0.25f)
                )
                
                // Hair strands being cut
                repeat(12) { i ->
                    val x = width * 0.3f + i * 15f
                    val y = height * 0.2f + sin(i * 0.3f) * 20f
                    
                    val hairStrand = Path().apply {
                        moveTo(x, y)
                        quadraticBezierTo(
                            x + 5f + sin(i * 0.7f) * 10f, 
                            y + 30f,
                            x + 10f + cos(i * 0.5f) * 8f, 
                            y + 60f
                        )
                        quadraticBezierTo(
                            x + 15f + sin(i * 0.9f) * 12f, 
                            y + 90f,
                            x + 20f, 
                            y + 120f
                        )
                    }
                
                drawPath(
                        path = hairStrand,
                        color = Color.White.copy(alpha = 0.15f + (i % 4) * 0.05f),
                        style = Stroke(width = 1.5f + (i % 3) * 0.5f, cap = StrokeCap.Round)
                    )
                }
                
                // Hair styling tools
                repeat(3) { i ->
                    val x = width * (0.6f + i * 0.15f)
                    val y = height * 0.8f
                    
                    when (i) {
                        0 -> {
                            // Hair brush
                            drawRect(
                                color = Color.White.copy(alpha = 0.2f),
                                topLeft = Offset(x - 8f, y - 15f),
                                size = Size(16f, 30f)
                            )
                            // Brush bristles
                            repeat(8) { b ->
                                drawLine(
                                    color = Color.White.copy(alpha = 0.15f),
                                    start = Offset(x - 6f + b * 1.5f, y - 10f),
                                    end = Offset(x - 6f + b * 1.5f, y + 5f),
                                    strokeWidth = 0.5f
                                )
                            }
                        }
                        1 -> {
                            // Hair comb
                            drawRect(
                                color = Color.White.copy(alpha = 0.2f),
                                topLeft = Offset(x - 6f, y - 12f),
                                size = Size(12f, 8f)
                            )
                            // Comb teeth
                            repeat(6) { t ->
                                drawLine(
                                    color = Color.White.copy(alpha = 0.18f),
                                    start = Offset(x - 5f + t * 2f, y - 4f),
                                    end = Offset(x - 5f + t * 2f, y + 8f),
                                    strokeWidth = 1f
                                )
                            }
                        }
                        2 -> {
                            // Hair clip
                            drawOval(
                                color = Color.White.copy(alpha = 0.2f),
                                topLeft = Offset(x - 5f, y - 8f),
                                size = Size(10f, 16f)
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.15f),
                                start = Offset(x - 3f, y - 5f),
                                end = Offset(x + 3f, y + 5f),
                                strokeWidth = 1f
                            )
                        }
                    }
                }
                
                // Hair texture dots
                repeat(20) { i ->
                    val x = (i * 43) % width.toInt().toFloat()
                    val y = (i * 67) % height.toInt().toFloat()
                    
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = 1f + (i % 3) * 0.5f,
                        center = Offset(x, y)
                    )
                }
            }
            
            "wand" -> {
                // Enhanced Art/Creative background - Studio Ghibli inspired
                
                // Magic sparkles and stars
                repeat(15) { i ->
                    val x = (i * 67) % width.toInt().toFloat()
                    val y = (i * 43) % height.toInt().toFloat()
                    
                    // Four-pointed star
                    val starPath = Path().apply {
                        val centerX = x
                        val centerY = y
                        val size = 4f + (i % 3) * 2f
                        
                        moveTo(centerX, centerY - size)
                        lineTo(centerX + size * 0.3f, centerY - size * 0.3f)
                        lineTo(centerX + size, centerY)
                        lineTo(centerX + size * 0.3f, centerY + size * 0.3f)
                        lineTo(centerX, centerY + size)
                        lineTo(centerX - size * 0.3f, centerY + size * 0.3f)
                        lineTo(centerX - size, centerY)
                        lineTo(centerX - size * 0.3f, centerY - size * 0.3f)
                        close()
                    }
                    
                    drawPath(
                        path = starPath,
                        color = Color.White.copy(alpha = 0.2f + (i % 4) * 0.05f)
                    )
                }
                
                // Magic wand
                val wandX = width * 0.2f
                val wandY = height * 0.3f
                
                // Wand handle
                drawLine(
                    color = Color.White.copy(alpha = 0.3f),
                    start = Offset(wandX, wandY),
                    end = Offset(wandX + 40f, wandY + 60f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                
                // Wand tip star
                val tipX = wandX + 40f
                val tipY = wandY + 60f
                val tipPath = Path().apply {
                    moveTo(tipX, tipY - 8f)
                    lineTo(tipX + 3f, tipY - 3f)
                    lineTo(tipX + 8f, tipY)
                    lineTo(tipX + 3f, tipY + 3f)
                    lineTo(tipX, tipY + 8f)
                    lineTo(tipX - 3f, tipY + 3f)
                    lineTo(tipX - 8f, tipY)
                    lineTo(tipX - 3f, tipY - 3f)
                    close()
                }
                
                drawPath(
                    path = tipPath,
                    color = Color.White.copy(alpha = 0.4f)
                )
                
                // Magic trails from wand
                repeat(8) { i ->
                    val trailPath = Path().apply {
                        val startX = tipX + cos(i * PI / 4).toFloat() * 15f
                        val startY = tipY + sin(i * PI / 4).toFloat() * 15f
                        
                        moveTo(startX, startY)
                        quadraticBezierTo(
                            startX + cos(i * PI / 4).toFloat() * 25f + sin(i * 0.5f) * 10f,
                            startY + sin(i * PI / 4).toFloat() * 25f + cos(i * 0.7f) * 10f,
                            startX + cos(i * PI / 4).toFloat() * 40f,
                            startY + sin(i * PI / 4).toFloat() * 40f
                        )
                    }
                    
                    drawPath(
                        path = trailPath,
                        color = Color.White.copy(alpha = 0.15f - i * 0.01f),
                        style = Stroke(width = 2f, cap = StrokeCap.Round)
                    )
                }
                
                // Floating art elements
                repeat(8) { i ->
                    val x = width * (0.5f + (i % 4) * 0.15f)
                    val y = height * (0.2f + (i / 4) * 0.4f)
                    
                    when (i % 4) {
                        0 -> {
                            // Paint brush
                            drawLine(
                                color = Color.White.copy(alpha = 0.25f),
                                start = Offset(x, y),
                                end = Offset(x + 20f, y - 5f),
                                strokeWidth = 2f,
                                cap = StrokeCap.Round
                            )
                            // Brush tip
                drawCircle(
                                color = Color.White.copy(alpha = 0.2f),
                                radius = 3f,
                                center = Offset(x + 20f, y - 5f)
                            )
                        }
                        1 -> {
                            // Color droplet
                            val dropletPath = Path().apply {
                                moveTo(x, y - 8f)
                                quadraticBezierTo(x - 6f, y, x, y + 8f)
                                quadraticBezierTo(x + 6f, y, x, y - 8f)
                            }
                            drawPath(
                                path = dropletPath,
                                color = Color.White.copy(alpha = 0.2f)
                            )
                        }
                        2 -> {
                            // Artist palette
                            drawOval(
                                color = Color.White.copy(alpha = 0.18f),
                                topLeft = Offset(x - 8f, y - 6f),
                                size = Size(16f, 12f)
                            )
                            // Palette hole
                            drawCircle(
                                color = Color.Transparent,
                                radius = 2f,
                                center = Offset(x + 4f, y)
                            )
                        }
                        3 -> {
                            // Pencil
                            drawLine(
                                color = Color.White.copy(alpha = 0.22f),
                                start = Offset(x, y),
                                end = Offset(x + 15f, y + 3f),
                                strokeWidth = 3f,
                                cap = StrokeCap.Round
                            )
                            // Pencil tip
                            drawCircle(
                                color = Color.White.copy(alpha = 0.3f),
                                radius = 1.5f,
                                center = Offset(x + 15f, y + 3f)
                            )
                        }
                    }
                }
                
                // Creative energy swirls
                repeat(4) { i ->
                    val centerX = width * (0.1f + i * 0.25f)
                    val centerY = height * 0.7f
                    
                    val swirlPath = Path()
                    var angle = 0f
                    var radius = 3f
                    
                    swirlPath.moveTo(centerX, centerY)
                    
                    while (angle < PI * 1.5) {
                        val x = centerX + cos(angle.toDouble()).toFloat() * radius
                        val y = centerY + sin(angle.toDouble()).toFloat() * radius
                        swirlPath.lineTo(x, y)
                        angle += 0.2f
                        radius += 0.8f
                    }
                    
                    drawPath(
                        path = swirlPath,
                        color = Color.White.copy(alpha = 0.12f),
                        style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundImagePattern(iconType: String) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height
        
        // Create background patterns to simulate the background images
        when (iconType) {
            "shirt" -> {
                // Clothes pattern - geometric shapes
                repeat(8) { i ->
                    val x = (i * width / 8) + (width / 16)
                    val y = height * 0.3f + sin(i * 0.5f) * 20f
                    
                    drawCircle(
                        color = Color(0x20FFFFFF),
                        radius = 15f + i * 2f,
                        center = Offset(x, y)
                    )
                }
                
                // Diagonal lines
                repeat(6) { i ->
                    val startX = i * width / 6
                    val startY = height * 0.1f
                    val endX = startX + width / 3
                    val endY = height * 0.9f
                    
                    drawLine(
                        color = Color(0x10FFFFFF),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 2f
                    )
                }
            }
            
            "scissors" -> {
                // Hair pattern - flowing curves
                repeat(5) { i ->
                    val path = Path().apply {
                        moveTo(0f, height * (0.2f + i * 0.15f))
                        quadraticBezierTo(
                            width * 0.5f, height * (0.1f + i * 0.15f),
                            width, height * (0.3f + i * 0.15f)
                        )
                    }
                    
                    drawPath(
                        path = path,
                        color = Color(0x15FFFFFF),
                        style = Stroke(width = 3f)
                    )
                }
                
                // Scattered dots for texture
                repeat(20) { i ->
                    val x = (i * 37) % width.toInt()
                    val y = (i * 43) % height.toInt()
                    
                    drawCircle(
                        color = Color(0x08FFFFFF),
                        radius = 3f + (i % 3),
                        center = Offset(x.toFloat(), y.toFloat())
                    )
                }
            }
            
            "wand" -> {
                // Art pattern - creative swirls and shapes
                repeat(4) { i ->
                    val centerX = width * (0.2f + i * 0.2f)
                    val centerY = height * 0.5f
                    
                    // Draw spiral
                    val path = Path()
                    var angle = 0f
                    var radius = 5f
                    
                    path.moveTo(centerX, centerY)
                    
                    while (angle < 4 * PI) {
                        val x = centerX + cos(angle) * radius
                        val y = centerY + sin(angle) * radius
                        path.lineTo(x, y)
                        angle += 0.2f
                        radius += 0.5f
                    }
                    
                    drawPath(
                        path = path,
                        color = Color(0x12FFFFFF),
                        style = Stroke(width = 2f)
                    )
                }
                
                // Abstract triangles
                repeat(6) { i ->
                    val centerX = (i * width / 6) + width / 12
                    val centerY = height * 0.2f + (i % 2) * height * 0.6f
                    val size = 15f + i * 3f
                    
                    val path = Path().apply {
                        moveTo(centerX, centerY - size)
                        lineTo(centerX - size, centerY + size)
                        lineTo(centerX + size, centerY + size)
                        close()
                    }
                    
                    drawPath(
                        path = path,
                        color = Color(0x0AFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun HtmlStyleIcon(iconType: String, primaryColor: Color) {
    when (iconType) {
        "shirt" -> {
            Icon(
                imageVector = Icons.Default.CheckCircle, // Best approximation for shirt
                contentDescription = "Clothes",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
        "scissors" -> {
            Icon(
                imageVector = Icons.Default.Face, // Face icon for hairstyle
                contentDescription = "Hairstyle",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
        "wand" -> {
            Icon(
                imageVector = Icons.Default.Create, // Create icon for art
                contentDescription = "Art",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


// Updated Preview
@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212,
    showSystemUi = true
)
@Composable
fun LandingPagePreview() {
    MaterialTheme {
        LandingPage(navController = rememberNavController())
    }
}
