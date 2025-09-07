package com.example.genme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.genme.R
import com.example.genme.utils.rememberImagePicker
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeardMakerPage(navController: NavController) {
    val bgStart = Color(0xFF1A1023)
    val bgEnd = Color(0xFF120B1A)
    val primary = Color(0xFF8C25F4)
    val textSubtle = Color(0xFFD9CBEF)

    var selectedBeard by remember { mutableStateOf("Select Beard Style") }
    val beardOptions = listOf("Select Beard Style", "Stubble", "Goatee", "Full Beard", "Van Dyke")
    var expanded by remember { mutableStateOf(false) }

    var photoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val pickFacePhoto = rememberImagePicker { uri -> photoUri = uri }

    Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(bgStart, bgEnd)))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom))
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp),
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
                        text = "Beard Maker",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(44.dp))
                }

                Spacer(Modifier.height(8.dp))

                // Upload area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color(0xFF4D3168), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Upload your face photo", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Upload a clear photo of your face to generate beard styles.",
                            color = textSubtle, fontSize = 13.sp, textAlign = TextAlign.Center
                        )
                    }

                    if (photoUri != null) {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "Selected face",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Button(
                        onClick = pickFacePhoto,
                        colors = ButtonDefaults.buttonColors(containerColor = primary),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Upload Photo", color = Color.White, fontWeight = FontWeight.Bold) }
                }

                Spacer(Modifier.height(16.dp))

                // Beard Style select
                Text("Beard Style", color = textSubtle, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 6.dp, bottom = 6.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedBeard,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2A1D3A)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF2A1D3A),
                            unfocusedContainerColor = Color(0xFF2A1D3A)
                        ),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        beardOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { selectedBeard = option; expanded = false }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Generate button
                Button(
                    onClick = { /* TODO hook generation */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(12.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = primary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Generate Beard", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
            }
            // Bottom nav is shared from MainActivity Scaffold
        }
    }
}
