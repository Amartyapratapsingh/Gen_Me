package com.example.genme

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.genme.ui.*
import com.example.genme.utils.rememberImagePicker
import com.example.genme.viewmodel.HairstyleViewModel
import com.example.genme.viewmodel.HairstyleViewModelFactory

@Composable
fun HairstyleChangePage(
    navController: NavController,
    application: Application = LocalContext.current.applicationContext as Application
) {
    val viewModel: HairstyleViewModel = remember {
        HairstyleViewModelFactory(application).create(HairstyleViewModel::class.java)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    val imagePicker = rememberImagePicker { uri ->
        uri?.let { viewModel.setPersonImage(it) }
    }
    
    // State for hairstyle selection
    var selectedGender by remember { mutableStateOf("Women's Styles") }
    var selectedHairstyle by remember { mutableStateOf("Long Bob") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    
    // Sample hairstyles data
    val womenStyles = listOf(
        "Long Bob", "Pixie Cut", "Beach Waves", "Curtain Bangs", "Shag Haircut",
        "French Bob", "Wolf Cut", "Butterfly Layers", "Modern Mullet", "Blunt Cut",
        "Layered Cut", "Wavy Bob", "Straight Long Hair", "Feathered Layers", "Textured Crop",
        "Afro", "Braided Crown", "Twisted Updo", "Sleek Ponytail", "Messy Bun",
        "Half Up Half Down", "Side Swept Bangs", "Full Fringe", "Wispy Bangs", "Micro Bangs",
        "Vintage Waves", "Pin Curls", "Victory Rolls", "Finger Waves", "Hollywood Glamour",
        "Boho Braids", "Dutch Braids", "French Braids", "Fishtail Braid", "Waterfall Braid",
        "Space Buns", "Top Knot", "Low Chignon", "High Ponytail", "Low Ponytail",
        "Crimped Hair", "Spiral Curls", "Tight Curls", "Loose Waves", "Straight Hair",
        "Asymmetrical Cut", "Undercut", "Fade Cut"
    )
    
    val menStyles = listOf(
        "Fade", "High Fade", "Low Fade", "Mid Fade", "Buzz Cut", "Crew Cut",
        "Pompadour", "Quiff", "Undercut", "Side Part", "Slicked Back", "Textured Crop",
        "French Crop", "Caesar Cut", "Ivy League", "Brush Cut", "Flat Top", "Mohawk",
        "Faux Hawk", "Man Bun", "Top Knot", "Long Hair", "Shag", "Mullet",
        "Modern Mullet", "Business Professional", "Classic Taper", "Skin Fade", "Bald Fade",
        "Drop Fade", "Burst Fade", "Temple Fade", "Beard Fade", "Disconnected Undercut",
        "Comb Over", "Hard Part", "Soft Part", "Messy Hair", "Tousled", "Wavy Hair",
        "Curly Top", "Afro", "Twist Out", "Lock Style", "Cornrows", "Buzz with Beard",
        "Long on Top Short Sides", "Textured Fringe", "Angular Fringe", "Blunt Fringe",
        "Side Swept", "Spiky Hair", "Wet Look", "Matte Finish"
    )
    
    val hairstyles = if (selectedGender == "Women's Styles") womenStyles else menStyles

    // Glassmorphic background with gradient blobs
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0C14))
            .drawBehind {
                // Top-left gradient blob
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x6022D3EE), Color(0x507C3AED), Color(0x20764BA2), Color.Transparent),
                        center = Offset(-200f, -200f),
                        radius = 600f
                    ),
                    radius = 600f,
                    center = Offset(-200f, -200f)
                )
                // Bottom-right gradient blob
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x603B82F6), Color(0x507C3AED), Color(0x20667EEA), Color.Transparent),
                        center = Offset(size.width + 200f, size.height + 200f),
                        radius = 700f
                    ),
                    radius = 700f,
                    center = Offset(size.width + 200f, size.height + 200f)
                )
                // Additional subtle middle gradient for depth
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x30A855F7), Color.Transparent),
                        center = Offset(size.width * 0.7f, size.height * 0.3f),
                        radius = 400f
                    ),
                    radius = 400f,
                    center = Offset(size.width * 0.7f, size.height * 0.3f)
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            GlasmorphicPageHeader(
                title = "Hairstyle Generator",
                subtitle = "Create stunning hairstyles",
                navController = navController,
                primaryColor = Color(0xFF00F6FF)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Upload Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { imagePicker() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.personImageUri != null) {
                            AsyncImage(
                                model = uiState.personImageUri,
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    modifier = Modifier.size(80.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White.copy(alpha = 0.05f)
                                    ),
                                    border = BorderStroke(
                                        2.dp,
                                        Color.White.copy(alpha = 0.2f)
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add Photo",
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "Add Photo",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Text(
                                    text = "Upload image for hairstyle",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Gender Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Men's Styles Button
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedGender = "Men's Styles" },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedGender == "Men's Styles") 
                                    Color(0x2600F6FF) else Color.White.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedGender == "Men's Styles") 
                                    Color(0xFF00F6FF) else Color.White.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = "Men's Styles",
                                color = if (selectedGender == "Men's Styles") Color.White else Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = if (selectedGender == "Men's Styles") FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Women's Styles Button
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedGender = "Women's Styles" },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedGender == "Women's Styles") 
                                    Color(0x2600F6FF) else Color.White.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedGender == "Women's Styles") 
                                    Color(0xFF00F6FF) else Color.White.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = "Women's Styles",
                                color = if (selectedGender == "Women's Styles") Color.White else Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = if (selectedGender == "Women's Styles") FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Hairstyle Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
            Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Select Hairstyle",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { dropdownExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    Color.White.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedHairstyle,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    
                                    Icon(
                                        Icons.Default.ExpandMore,
                                        contentDescription = "Expand",
                                        tint = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                                    .width(280.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Color(0xE6282828),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                hairstyles.forEach { style ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = style,
                                                color = if (style == selectedHairstyle) Color(0xFF00F6FF) else Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = if (style == selectedHairstyle) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            selectedHairstyle = style
                                            viewModel.setHairstyleText(style)
                                            dropdownExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(
                                                if (style == selectedHairstyle) 
                                                    Color(0x3300F6FF) 
                                                else 
                                                    Color.Transparent
                                            )
                                    )
                                }
                            }
                        }
                    }
                }

                // Generate Button - floating action style
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (uiState.personImageUri != null) {
                                viewModel.startHairstyleChange()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF00B8FF),  // App cyan
                                            Color(0xFF8338EC),  // App purple
                                            Color(0xFF00F5D4)   // App teal
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = "Generate",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Generate Hairstyle",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                // Space for main bottom navigation
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
