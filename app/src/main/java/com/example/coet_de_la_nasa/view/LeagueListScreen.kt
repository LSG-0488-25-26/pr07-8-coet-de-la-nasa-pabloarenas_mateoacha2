package com.example.coet_de_la_nasa.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coet_de_la_nasa.nav.Routes
import com.example.coet_de_la_nasa.viewmodel.FootballViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val vm: FootballViewModel = viewModel()
    val leagues = vm.leagues.observeAsState(emptyList())
    val isLoading = vm.isLoading.observeAsState(false)
    val error = vm.error.observeAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Football Leagues") })
        },
        modifier = modifier
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error.value != null -> {
                    Text(
                        text = error.value ?: "Error",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(leagues.value) { league ->
                            LeagueCard(league = league) {
                                navController.navigate(
                                    Routes.LeagueDetail.createRoute(
                                        leagueId = league.id,
                                        leagueName = league.name
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { vm.loadLeagues() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                enabled = !isLoading.value
            ) {
                Text("Recargar Ligas")
            }
        }
    }
}
