package com.example.coet_de_la_nasa.view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coet_de_la_nasa.model.ReleaseGroupDetailUi
import com.example.coet_de_la_nasa.util.decodeFromNav
import com.example.coet_de_la_nasa.viewmodel.MusicViewModel
import com.example.coet_de_la_nasa.viewmodel.MusicViewModelFactory

private const val COVER_ART_BASE = "https://coverartarchive.org/release-group/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDetailScreen(
    navController: NavController,
    mbid: String,
    titleEncoded: String,
    artistNameEncoded: String,
    modifier: Modifier = Modifier
) {
    val title = decodeFromNav(titleEncoded)
    val artistName = decodeFromNav(artistNameEncoded)
    val coverUrl = "$COVER_ART_BASE$mbid/front-500.jpg"

    val app = LocalContext.current.applicationContext as Application
    val vm: MusicViewModel = viewModel(factory = MusicViewModelFactory(app))
    val detail = vm.releaseGroupDetail.observeAsState(null)
    val detailLoading = vm.detailLoading.observeAsState(false)

    LaunchedEffect(mbid) {
        vm.loadDetail(mbid)
    }
    DisposableEffect(Unit) {
        onDispose { vm.clearDetail() }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(detail.value?.title ?: title) }) },
        modifier = modifier
    ) { padding ->
        when {
            detailLoading.value -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            detail.value != null -> {
                DetailContent(
                    detail = detail.value!!,
                    modifier = Modifier.padding(padding),
                    onAddToCollection = {
                        vm.addToCollection(
                            detail.value!!.mbid,
                            detail.value!!.title,
                            detail.value!!.artistName,
                            detail.value!!.coverUrl
                        )
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            else -> {
                FallbackDetailContent(
                    title = title,
                    artistName = artistName,
                    coverUrl = coverUrl,
                    modifier = Modifier.padding(padding),
                    onAddToCollection = {
                        vm.addToCollection(mbid, title, artistName, coverUrl)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    detail: ReleaseGroupDetailUi,
    modifier: Modifier = Modifier,
    onAddToCollection: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = detail.coverUrl,
            contentDescription = detail.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = detail.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = detail.artistName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (detail.primaryType != null || !detail.firstReleaseDate.isNullOrBlank()) {
            Text(
                text = listOfNotNull(detail.primaryType, detail.firstReleaseDate).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        if (!detail.disambiguation.isNullOrBlank()) {
            Text(
                text = detail.disambiguation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (detail.secondaryTypes?.isNotEmpty() == true) {
            Text(
                text = "Tipus: ${detail.secondaryTypes.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        if (detail.genres.isNotEmpty()) {
            Text(
                text = "Genres: ${detail.genres.take(10).joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (detail.tags.isNotEmpty()) {
            Text(
                text = "Tags: ${detail.tags.take(10).joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        if (detail.ratingValue != null && detail.ratingVotes > 0) {
            Text(
                text = "Valoracio: ${detail.ratingValue} (${detail.ratingVotes} vots)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        if (!detail.annotation.isNullOrBlank()) {
            Text(
                text = "Anotacio: ${detail.annotation.take(500)}${if (detail.annotation.length > 500) "..." else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        }
        if (detail.releases.isNotEmpty()) {
            Text(
                text = "Releases (${detail.releases.size}):",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            detail.releases.take(15).forEach { r ->
                Text(
                    text = "${r.title ?: r.id ?: "-"} · ${r.date ?: "-"} · ${r.status ?: "-"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                )
            }
            if (detail.releases.size > 15) {
                Text(
                    text = "... i ${detail.releases.size - 15} mes",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                )
            }
        }
        if (detail.aliases.isNotEmpty()) {
            Text(
                text = "Alias: ${detail.aliases.take(10).joinToString(", ")}${if (detail.aliases.size > 10) "..." else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
        if (detail.relations.isNotEmpty()) {
            Text(
                text = "Relacions (${detail.relations.size}):",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )
            detail.relations.take(10).forEach { rel ->
                val line = buildString {
                    append(rel.type ?: "-")
                    rel.direction?.let { append(" ($it)") }
                    rel.url?.let { append(": $it") }
                }
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                )
            }
            if (detail.relations.size > 10) {
                Text(
                    text = "... i ${detail.relations.size - 10} mes",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onAddToCollection) {
            Text("Afegir a la colleccio")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}

@Composable
private fun FallbackDetailContent(
    title: String,
    artistName: String,
    coverUrl: String,
    modifier: Modifier = Modifier,
    onAddToCollection: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = coverUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddToCollection) {
            Text("Afegir a la colleccio")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}
