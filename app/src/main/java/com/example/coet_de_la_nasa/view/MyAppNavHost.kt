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
        startDestination = Routes.LeagueList.route,
        modifier = modifier
    ) {
        composable(Routes.LeagueList.route) {
            LeagueListScreen(navController = navController)
        }

        composable(Routes.Colleccio.route) {
            ColleccioScreen(navController = navController)
        }

        composable(
            route = Routes.LeagueDetail.route,
            arguments = listOf(
                navArgument("mbid") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("artistName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            LeagueDetailScreen(
                navController = navController,
                mbid = backStackEntry.arguments?.getString("mbid") ?: "",
                titleEncoded = backStackEntry.arguments?.getString("title") ?: "",
                artistNameEncoded = backStackEntry.arguments?.getString("artistName") ?: ""
            )
        }
    }
}

