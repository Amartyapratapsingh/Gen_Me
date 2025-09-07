package com.example.genme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.genme.ui.GalleryScreen
import com.example.genme.ui.theme.GenMeTheme
import com.example.genme.viewmodel.TryOnViewModel
import com.example.genme.viewmodel.TryOnViewModelFactory

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.genme.ui.FullScreenImageViewer
import com.example.genme.ui.SettingsPage
import com.example.genme.ui.NeonGlassBottomNav
import com.example.genme.ui.CoinPurchasePage
import com.example.genme.ui.GenerateOptionsPopup
import com.example.genme.ui.SplashScreen
import com.example.genme.ui.BeardMakerPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display to remove gaps
        enableEdgeToEdge()
        
        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val viewModel: TryOnViewModel by lazy {
            ViewModelProvider(this, TryOnViewModelFactory(application)).get(TryOnViewModel::class.java)
        }
        
        setContent {
            GenMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    
                    // State for Generate Options Popup
                    var showGeneratePopup by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            if (currentRoute != "splash") {
                                NeonGlassBottomNav(
                                    navController = navController,
                                    currentRoute = currentRoute,
                                    onShowGeneratePopup = { showGeneratePopup = true }
                                )
                            }
                        }
                    ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "splash",
                                modifier = Modifier.fillMaxSize().padding(innerPadding)
                            ) {
                                composable("splash") { SplashScreen(navController = navController) }
                                composable("landing_page") { LandingPage(navController = navController) }
                                composable("clothes_change") { ClothesChangePage(navController = navController) }
                                composable("hairstyle_change") { HairstyleChangePage(navController = navController) }
                                composable("ghibli_art") { GhibliArtPage(navController = navController) }
                                composable("beard_maker") { BeardMakerPage(navController = navController) }
                                composable("coins") { CoinPurchasePage(navController = navController) }
                                composable("gallery") { GalleryScreen(viewModel = viewModel, navController = navController) }
                                composable("settings") { SettingsPage(navController = navController, viewModel = viewModel) }
                            // profile route removed; use coins page instead via bottom nav
                            composable(
                                "full_screen_image/{imagePath}",
                                arguments = listOf(navArgument("imagePath") { type = NavType.StringType })
                            ) { backStackEntry ->
                                FullScreenImageViewer(
                                    navController = navController,
                                    imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""
                                )
                            }
                        }
                        }
                        
                        // Generate Options Popup Overlay
                        if (showGeneratePopup) {
                            GenerateOptionsPopup(
                                onDismiss = { showGeneratePopup = false },
                                onGenerateOutfit = { navController.navigate("clothes_change") },
                                onGenerateHairstyle = { navController.navigate("hairstyle_change") },
                                onBeardMaker = { navController.navigate("beard_maker") }
                            )
                        }
                    }
                }
            }
        }
    }
}
