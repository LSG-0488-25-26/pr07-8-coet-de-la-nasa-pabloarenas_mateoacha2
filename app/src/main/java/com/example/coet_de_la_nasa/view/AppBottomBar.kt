package com.example.coet_de_la_nasa.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.coet_de_la_nasa.nav.Routes

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.LeagueList.route,
            onClick = {
                if (currentRoute != Routes.LeagueList.route) {
                    navController.navigate(Routes.LeagueList.route) {
                        popUpTo(Routes.LeagueList.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Default.Album, contentDescription = "Albums") },
            label = { Text("Albums") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.Colleccio.route,
            onClick = {
                if (currentRoute != Routes.Colleccio.route) {
                    navController.navigate(Routes.Colleccio.route) {
                        popUpTo(Routes.LeagueList.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Default.Collections, contentDescription = "Colleccio") },
            label = { Text("Colleccio") }
        )
    }
}
