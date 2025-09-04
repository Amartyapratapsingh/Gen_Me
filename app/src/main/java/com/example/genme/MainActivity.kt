package com.example.genme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.genme.ui.GalleryScreen
import com.example.genme.ui.theme.GenMeTheme
import com.example.genme.viewmodel.TryOnViewModel
import com.example.genme.viewmodel.TryOnViewModelFactory

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.genme.ui.FullScreenImageViewer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
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
                    NavHost(navController = navController, startDestination = "landing_page") {
                        composable("landing_page") {
                            LandingPage(navController = navController)
                        }
                        composable("clothes_change") {
                            ClothesChangePage(navController = navController)
                        }
                        composable("hairstyle_change") {
                            HairstyleChangePage(navController = navController)
                        }
                        composable("ghibli_art") {
                            GhibliArtPage(navController = navController)
                        }
                        composable("gallery") {
                            GalleryScreen(viewModel = viewModel, navController = navController)
                        }
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
            }
        }
    }
}
