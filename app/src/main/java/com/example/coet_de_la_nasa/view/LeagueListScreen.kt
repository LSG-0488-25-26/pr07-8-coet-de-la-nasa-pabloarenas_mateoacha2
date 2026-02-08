package com.example.coet_de_la_nasa.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.app.Application
import com.example.coet_de_la_nasa.nav.Routes
import com.example.coet_de_la_nasa.util.encodeForNav
import com.example.coet_de_la_nasa.viewmodel.MusicViewModel
import com.example.coet_de_la_nasa.viewmodel.MusicViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val app = LocalContext.current.applicationContext as Application
    val vm: MusicViewModel = viewModel(factory = MusicViewModelFactory(app))
    val releaseGroups = vm.releaseGroups.observeAsState(emptyList())
    val isLoading = vm.isLoading.observeAsState(false)
    val error = vm.error.observeAsState(null)
    val query = vm.currentQuery.observeAsState("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MusicBrainz - Albums") },
                actions = {
                    TextButton(onClick = { navController.navigate(Routes.Colleccio.route) }) {
                        Text("La meva colleccio")
                    }
                }
            )
        },
        bottomBar = { AppBottomBar(navController = navController, currentRoute = Routes.LeagueList.route) },
        modifier = modifier
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                SearchBar(
                    query = query.value,
                    onQueryChange = { vm.setQuery(it) },
                    onSearch = { vm.search(query.value) },
                    enabled = !isLoading.value && query.value.isNotBlank()
                )

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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = error.value ?: "Error",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(onClick = { vm.search(query.value) }) {
                                Text("Tornar a intentar")
                            }
                        }
                    }
                    else -> {
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val horizontalPadding = if (maxWidth < 600.dp) 16.dp else 48.dp
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 72.dp, start = horizontalPadding, end = horizontalPadding),
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
}
