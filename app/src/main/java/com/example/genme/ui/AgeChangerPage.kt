package com.example.genme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.genme.utils.rememberImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeChangerPage(navController: NavController) {
    val bg = Color(0xFF120D1B)
    val primary = Color(0xFF8C25F4)
    val pink = Color(0xFFFF25A8)

    var faceUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val pickFace = rememberImagePicker { uri -> faceUri = uri }

    var expanded by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var selectedAge by remember { mutableStateOf<Int?>(null) }
    val ages = remember { (1..100).toList() }
    val filtered = remember(search) { 
        if (search.isEmpty()) {
            ages
        } else {
            ages.filter { it.toString().startsWith(search, ignoreCase = true) }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(bg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom))
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(44.dp).clickable { navController.popBackStack() },
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                    Text(
                        text = "Age Changer",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(44.dp))
                }

                // Intro text
                Text(
                    text = "Upload a photo to watch the magic happen.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    textAlign = TextAlign.Center
                )

                // Upload card
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.2f))
                        .border(
                            BorderStroke(2.dp, Brush.horizontalGradient(listOf(primary, pink))),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { pickFace() }
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (faceUri == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(80.dp))
                            Text("Tap to upload a photo", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("PNG, JPG, HEIC up to 10MB", color = Color(0xFF9CA3AF), fontSize = 14.sp)
                        }
                    } else {
                        AsyncImage(
                            model = faceUri,
                            contentDescription = "Selected photo",
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Age selector
                Text("Select Age", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Search + dropdown
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = if (search.isNotEmpty()) search else (selectedAge?.toString() ?: ""),
                            onValueChange = { newValue ->
                                // Only allow numbers and limit to 3 digits max
                                if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 3)) {
                                    search = newValue
                                    expanded = true
                                    // If user types a complete valid age, auto-select it
                                    val age = newValue.toIntOrNull()
                                    if (age != null && age in 1..100) {
                                        selectedAge = age
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primary,
                                unfocusedBorderColor = Color(0xFF3F3F46),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1C1628).copy(alpha = 0.8f),
                                unfocusedContainerColor = Color(0xFF1C1628).copy(alpha = 0.8f),
                                focusedPlaceholderColor = Color(0xFF9CA3AF),
                                unfocusedPlaceholderColor = Color(0xFF9CA3AF)
                            ),
                            placeholder = { Text("Type age (1-100) or select", color = Color(0xFF9CA3AF)) },
                            trailingIcon = { 
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                ) 
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded, 
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color(0xFF1C1628))
                                .heightIn(max = 300.dp)
                        ) {
                            if (filtered.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No ages found", color = Color(0xFF9CA3AF)) },
                                    onClick = { }
                                )
                            } else {
                                filtered.forEach { age ->
                                    DropdownMenuItem(
                                        text = { Text("$age years old", color = Color.White) },
                                        onClick = {
                                            selectedAge = age
                                            search = ""
                                            expanded = false
                                        },
                                        colors = MenuDefaults.itemColors(
                                            textColor = Color.White,
                                            leadingIconColor = Color.White,
                                            trailingIconColor = Color.White
                                        ),
                                        modifier = Modifier.background(Color(0xFF1C1628))
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            // Bottom fixed generate button (nav bar comes from Scaffold)
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Button(
                    onClick = { /* TODO: start age change */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(16.dp, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFEC4899)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Generate", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

