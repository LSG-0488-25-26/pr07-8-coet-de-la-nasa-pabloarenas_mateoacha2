package com.example.coet_de_la_nasa.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coet_de_la_nasa.nav.Routes
import com.example.coet_de_la_nasa.util.encodeForNav
import com.example.coet_de_la_nasa.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val vm: MusicViewModel = viewModel()
    val releaseGroups = vm.releaseGroups.observeAsState(emptyList())
    val isLoading = vm.isLoading.observeAsState(false)
    val error = vm.error.observeAsState(null)
    var query by remember { mutableStateOf("rock") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MusicBrainz - Álbumes") })
        },
        modifier = modifier
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar álbumes (ej: rock, beatles)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )
                Button(
                    onClick = { vm.search(query) },
                    modifier = Modifier.padding(bottom = 16.dp),
                    enabled = !isLoading.value && query.isNotBlank()
                ) {
                    Text("Buscar")
                }

                when {
                    isLoading.value -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error.value != null -> {
                        Text(
                            text = error.value ?: "Error",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 72.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(releaseGroups.value) { item ->
                                LeagueCard(
                                    title = item.title,
                                    artistName = item.artistName,
                                    coverUrl = item.coverUrl
                                ) {
                                    navController.navigate(
                                        Routes.LeagueDetail.createRoute(
                                            mbid = item.mbid,
                                            title = encodeForNav(item.title),
                                            artistName = encodeForNav(item.artistName)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
