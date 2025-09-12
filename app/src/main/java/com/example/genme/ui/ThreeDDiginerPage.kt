package com.example.genme.ui

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.genme.utils.rememberImagePicker
import androidx.compose.ui.platform.LocalContext
import android.app.Application
import androidx.compose.ui.graphics.asImageBitmap
import com.example.genme.viewmodel.FigurineViewModel
import com.example.genme.viewmodel.FigurineViewModelFactory

@Composable
fun ThreeDDiginerPage(navController: NavController, application: Application = LocalContext.current.applicationContext as Application) {
    val bg = Color(0xFF0F0B14)
    val primary = Color(0xFF8C25F4)
    val cardBg = Color(0xFF1A1023).copy(alpha = 0.5f)
    val viewModel: FigurineViewModel = remember { FigurineViewModelFactory(application).create(FigurineViewModel::class.java) }
    val uiState by viewModel.uiState.collectAsState()
    val launchPicker = rememberImagePicker { uri -> uri?.let { viewModel.setPersonImage(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1C1424), Color.Transparent)
                    )
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(
                        text = "3D Figurine Generator",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pick a style and generate",
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.width(40.dp))
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Image Upload
            Surface(color = cardBg, shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionTitle("Image Upload")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFF3F3F46), RoundedCornerShape(16.dp))
                        .background(Color(0xFF0B0B0F))
                        .clickable { launchPicker() },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.personImageUri != null) {
                        AsyncImage(
                            model = uiState.personImageUri,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x99000000))
                                .clickable { launchPicker() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) { Text("Change", color = Color.White, fontSize = 12.sp) }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(40.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Tap to upload a photo", color = Color(0xFF9CA3AF))
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    FilledTonalButton(
                        onClick = { launchPicker() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF27272A), contentColor = Color.White)
                    ) { Icon(Icons.Default.PhotoLibrary, contentDescription = null); Spacer(Modifier.width(6.dp)); Text("Choose Photo") }
                    FilledTonalButton(
                        onClick = { launchPicker() }, // simple fallback to gallery
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF27272A), contentColor = Color.White)
                    ) { Icon(Icons.Default.PhotoCamera, contentDescription = null); Spacer(Modifier.width(6.dp)); Text("Take Photo") }
                }
            }
            }

            // Style Selection
            Surface(color = cardBg, shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitle("Style Selection")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Creative Scenes", color = Color(0xFFE5E7EB), fontWeight = FontWeight.SemiBold)
                    // label to style-key pairs defined by backend
                    val scenes: List<Pair<String, String>> = listOf(
                        "Default — Computer Desk" to "default",
                        "Chibi — Cozy Collector's Room" to "chibi",
                        "Anime Store Display" to "store",
                        "Collector's Trophy Room" to "trophy",
                        "Artist Workshop" to "workshop",
                        "Japanese Otaku Room" to "otaku"
                    )
                    FlowWrap(spacing = 8.dp) {
                        scenes.forEach { (label, code) ->
                            val selected = uiState.selectedStyle == code
                            SceneChip(label = label, selected = selected) { viewModel.setStyle(code) }
                        }
                    }
                }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Anime Character Transformations", color = Color(0xFFE5E7EB), fontWeight = FontWeight.SemiBold)
                        val avatars: List<Triple<String, String, String>> = listOf(
                            Triple("Dragon Ball - Goku", "goku", "https://lh3.googleusercontent.com/aida-public/AB6AXuDiCPX92-3vQHRgBT-J4Kb8w1WJEQIw-AflDEmBcgdcz5qZrhNnxFEDxKgX9yGY79qo2VNa5Mp0O0tghANCLxy8rzeqxzp1ysSl-KnD7LxcsqAzVkMD7VXd63SmJ0xRCuQkb3n0Q_2oEbke2YkHyxu_gqz8P4qXXmrpm28CcbQt0I9wkpPp9fu3DDsUUHEuRBsEC0uElVRo2KlVeHEqfCuPY_97DNAiWMPzwwsmlnRwcnlkj8GAlc_73Be-lKHHfwgEYE4NxESN6-0"),
                            Triple("One Piece - Luffy", "luffy", "https://lh3.googleusercontent.com/aida-public/AB6AXuCfxIsgjST84pEd5ecCfA5ZE25b9zEgStIv2WbTt9x1IFj_fpiuavnEo9Ge5hg39iYVU5LjCQ7X8DFxbnc_UcsHFO1JvmgQNGrvRcWDYeRyRKOYvC2F_FHL7BdeFQtZRDRaYvVp0ImkH9-pxB3MYuOTpktEW8T9m9FxVZkeSsYdOVG_-D68QfXPJbb7p4X3nOWwsKzKjTWCXmUwkUFwrLosnM9nwzEdwxxL7BgzQhQFy-H5mQuKRAEiST4tLutKy4gdZYCUUiVXkq8"),
                            Triple("Naruto", "naruto", "https://lh3.googleusercontent.com/aida-public/AB6AXuD-2zy7B6lhl1uh4etNCtYo5hMgcjsfT0gVZeZRxVPxHUOJ27aFNnvGkaPfok0qipsswHUhdaw-TLKq7kKYxrqoU6Q61U24DynWoIaEp0tbFvtCrypIxBoLrbzJt_LM832wlHJNxoNq5QtLYYrLV7ge0tn0U4IUfJ_gs6d9-uRtcYciUM0woO7XmP0oOUx8bHNOJOV-e57qjk9POL-2ltIiFZKRMkvuMkC3db4LiMdtZHAgVmtnZbx5gATOBGmwgRmbfynY3-alMD0"),
                            Triple("Bleach - Ichigo", "ichigo", "https://lh3.googleusercontent.com/aida-public/AB6AXuBbxdHhgPHHX5Ioqdngx7z7IR999dPtL2DP4JS24ddVN89k7rCZXTDkVGH3UFx7NSvpOVhFLMRFpPYiLjDDEeH1mZYwEkt-F01S07g7Imv4xMfS2EgGZQU10e8CTaBU347mmptCxk2WFONxkXoTpQQxam4mQUWrbZ8rkPVDGMeyX6yr_VDnT8xSz06CIh37TGM80wqtGakO5aY9C9pJfla_LhKam73qCJez-WQYq-QFJWYHCh9e2GLu_dUbKo1p0Dop6CgNIhY8cRQ")
                        )
                        GridTwoColumns(items = avatars) { (title, code, url) ->
                            val selected = uiState.selectedStyle == code
                            AvatarChoice(title = title, imageUrl = url, selected = selected) { viewModel.setStyle(code) }
                        }
                    }

                }
            }

            // Generation
            Surface(color = cardBg, shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val enabled = uiState.personImageUri != null && uiState.selectedStyle.isNotBlank() && !uiState.isLoading
                    Button(
                        onClick = { viewModel.startGeneration() },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.White, disabledContainerColor = Color(0xFF4B227D))
                    ) {
                        Text("Generate 3D Figurine", fontWeight = FontWeight.Bold)
                    }
                    if (uiState.isLoading) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(uiState.loadingMessage ?: "Generating...", color = Color(0xFFE5E7EB), fontSize = 12.sp)
                            }
                            LinearProgressIndicator(color = primary, trackColor = Color(0xFF27272A), modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)))
                            Text("This may take a few minutes", color = Color(0xFF6B7280), fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
            }

            // Result
            Surface(color = cardBg, shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionTitle("Result")
                    if (uiState.resultImage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f / 3f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0B0B0F))
                        ) {
                            androidx.compose.foundation.Image(
                                bitmap = uiState.resultImage!!.asImageBitmap(),
                                contentDescription = "Generated 3D Figurine",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            FilledTonalButton(
                                onClick = { viewModel.saveResultToGallery() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF27272A), contentColor = Color.White)
                            ) { Text(if (uiState.isSaving) "Saving..." else "Download") }
                            FilledTonalButton(
                                onClick = { /* TODO: Share */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF27272A), contentColor = Color.White)
                            ) { Text("Share") }
                        }
                        OutlinedButton(
                            onClick = { viewModel.reset() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE5E7EB)),
                            border = BorderStroke(1.dp, Color(0xFF3F3F46))
                        ) { Text("Generate Another") }
                    } else {
                        Text("No result yet. Generate to see your 3D figurine.", color = Color(0xFFB6B6C8))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(2.dp))
    Divider(color = Color(0x22FFFFFF))
}

@Composable
private fun SceneChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) { { Icon(Icons.Default.Check, contentDescription = null) } } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFF27272A),
            selectedContainerColor = Color(0xFF3B0764),
            labelColor = Color.White,
            selectedLabelColor = Color.White,
            iconColor = Color.White
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = if (selected) Color(0xFF8C25F4) else Color.Transparent,
            selectedBorderColor = Color(0xFF8C25F4)
        )
    )
}

@Composable
private fun AvatarChoice(title: String, imageUrl: String, selected: Boolean, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (selected) Color(0xFF8C25F4) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (selected) Color(0xFF3B0764) else Color(0xFF27272A)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (selected) 4.dp else 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
        }
    }
}

@Composable
private fun <T> GridTwoColumns(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) { content(item) }
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FlowWrap(
    spacing: Dp,
    content: @Composable () -> Unit
) {
    // Simple single-row wrap for a small set of chips
    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) { content() }
}
