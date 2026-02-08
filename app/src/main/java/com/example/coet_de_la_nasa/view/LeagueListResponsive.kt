package com.example.coet_de_la_nasa.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi

/**
 * Vista para pantalla Compact: una sola columna (LazyColumn).
 */
@Composable
fun LeagueListCompactView(
    items: List<ReleaseGroupItemUi>,
    isLoading: Boolean,
    error: String?,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onItemClick: (ReleaseGroupItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            enabled = !isLoading && query.isNotBlank(),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                Button(onClick = onRetry, shape = RoundedCornerShape(12.dp)) { Text("Tornar a intentar") }
            }
            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 72.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    LeagueCard(
                        title = item.title,
                        artistName = item.artistName,
                        coverUrl = item.coverUrl,
                        primaryType = item.primaryType,
                        firstReleaseDate = item.firstReleaseDate
                    ) { onItemClick(item) }
                }
            }
        }
    }
}

/**
 * Vista para pantalla Medium: dos columnas (LazyVerticalGrid).
 */
@Composable
fun LeagueListMediumView(
    items: List<ReleaseGroupItemUi>,
    isLoading: Boolean,
    error: String?,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onItemClick: (ReleaseGroupItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            enabled = !isLoading && query.isNotBlank(),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                Button(onClick = onRetry, shape = RoundedCornerShape(12.dp)) { Text("Tornar a intentar") }
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 72.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    LeagueCard(
                        title = item.title,
                        artistName = item.artistName,
                        coverUrl = item.coverUrl,
                        primaryType = item.primaryType,
                        firstReleaseDate = item.firstReleaseDate
                    ) { onItemClick(item) }
                }
            }
        }
    }
}

/**
 * Vista para pantalla Extended: tres columnas (LazyVerticalGrid).
 */
@Composable
fun LeagueListExtendedView(
    items: List<ReleaseGroupItemUi>,
    isLoading: Boolean,
    error: String?,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onItemClick: (ReleaseGroupItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(32.dp)) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            enabled = !isLoading && query.isNotBlank(),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                Button(onClick = onRetry, shape = RoundedCornerShape(12.dp)) { Text("Tornar a intentar") }
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 72.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    LeagueCard(
                        title = item.title,
                        artistName = item.artistName,
                        coverUrl = item.coverUrl,
                        primaryType = item.primaryType,
                        firstReleaseDate = item.firstReleaseDate
                    ) { onItemClick(item) }
                }
            }
        }
    }
}
