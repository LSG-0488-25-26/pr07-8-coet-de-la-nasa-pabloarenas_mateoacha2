package com.example.coet_de_la_nasa.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coet_de_la_nasa.nav.Routes

@Composable
fun MyAppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DogList.route,
        modifier = modifier
    ) {
        composable(Routes.DogList.route) {
            DogListScreen(navController = navController)
        }

        composable(
            route = Routes.DogDetail.route,
            arguments = listOf(
                navArgument("breed") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DogDetailScreen(
                navController = navController,
                breed = backStackEntry.arguments?.getString("breed") ?: "",
                encodedImageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            )
        }
    }
}

