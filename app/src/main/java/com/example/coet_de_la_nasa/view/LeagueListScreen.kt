package com.example.coet_de_la_nasa.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.app.Application
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi
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
            androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val windowSize = windowSizeFromWidth(maxWidth)
                val onItemClick: (ReleaseGroupItemUi) -> Unit = { item ->
                    navController.navigate(
                        Routes.LeagueDetail.createRoute(
                            mbid = item.mbid,
                            title = encodeForNav(item.title),
                            artistName = encodeForNav(item.artistName)
                        )
                    )
                }
                when (windowSize) {
                    WindowSize.Compact -> LeagueListCompactView(
                        items = releaseGroups.value,
                        isLoading = isLoading.value,
                        error = error.value,
                        query = query.value,
                        onQueryChange = { vm.setQuery(it) },
                        onSearch = { vm.search(query.value) },
                        onRetry = { vm.search(query.value) },
                        onItemClick = onItemClick
                    )
                    WindowSize.Medium -> LeagueListMediumView(
                        items = releaseGroups.value,
                        isLoading = isLoading.value,
                        error = error.value,
                        query = query.value,
                        onQueryChange = { vm.setQuery(it) },
                        onSearch = { vm.search(query.value) },
                        onRetry = { vm.search(query.value) },
                        onItemClick = onItemClick
                    )
                    WindowSize.Extended -> LeagueListExtendedView(
                        items = releaseGroups.value,
                        isLoading = isLoading.value,
                        error = error.value,
                        query = query.value,
                        onQueryChange = { vm.setQuery(it) },
                        onSearch = { vm.search(query.value) },
                        onRetry = { vm.search(query.value) },
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}
