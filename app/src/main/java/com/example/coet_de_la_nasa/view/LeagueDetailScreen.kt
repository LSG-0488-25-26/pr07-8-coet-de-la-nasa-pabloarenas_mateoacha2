package com.example.coet_de_la_nasa.view

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coet_de_la_nasa.local.CollectionWithCount
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
    val detailError = vm.detailError.observeAsState(null)
    val collections by vm.collectionsWithCount.observeAsState(emptyList())
    var showAddToCollectionDialog by remember { mutableStateOf(false) }

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
                    onAddToCollection = { showAddToCollectionDialog = true },
                    onBack = { navController.popBackStack() }
                )
            }
            else -> {
                FallbackDetailContent(
                    title = title,
                    artistName = artistName,
                    coverUrl = coverUrl,
                    detailError = detailError.value,
                    onRetry = { vm.loadDetail(mbid) },
                    modifier = Modifier.padding(padding),
                    onAddToCollection = { showAddToCollectionDialog = true },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }

    if (showAddToCollectionDialog) {
        val mbidToAdd = detail.value?.mbid ?: mbid
        val titleToAdd = detail.value?.title ?: title
        val artistToAdd = detail.value?.artistName ?: artistName
        val coverToAdd = detail.value?.coverUrl ?: coverUrl
        CollectionPickerDialog(
            collections = collections,
            onSelect = { collectionId ->
                vm.addToCollection(mbidToAdd, titleToAdd, artistToAdd, coverToAdd, collectionId)
                showAddToCollectionDialog = false
                navController.popBackStack()
            },
            onDismiss = { showAddToCollectionDialog = false }
        )
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
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = detail.artistName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
        if (detail.primaryType != null || !detail.firstReleaseDate.isNullOrBlank()) {
            Text(
                text = listOfNotNull(detail.primaryType, detail.firstReleaseDate).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        if (!detail.disambiguation.isNullOrBlank()) {
            Text(
                text = detail.disambiguation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.secondaryTypes?.isNotEmpty() == true) {
            Text(
                text = "TIPO: ${detail.secondaryTypes.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.genres.isNotEmpty()) {
            Text(
                text = "GÉNEROS: ${detail.genres.take(10).joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.tags.isNotEmpty()) {
            Text(
                text = "ETIQUETAS: ${detail.tags.take(10).joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.ratingValue != null && detail.ratingVotes > 0) {
            Text(
                text = "VALORACIÓN: ${detail.ratingValue} (${detail.ratingVotes} votos)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
        if (!detail.annotation.isNullOrBlank()) {
            Text(
                text = "ANOTACIÓN: ${detail.annotation.take(500)}${if (detail.annotation.length > 500) "..." else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.releases.isNotEmpty()) {
            Text(
                text = "LANZAMIENTOS (${detail.releases.size}):",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
            detail.releases.take(15).forEach { r ->
                val statusEs = (r.status?.lowercase())?.let { when (it) {
                    "official" -> "Oficial"; "bootleg" -> "Bootleg"; "promotional" -> "Promocional"; else -> r.status
                } } ?: r.status
                val extra = listOfNotNull(r.country, r.barcode).joinToString(" · ").takeIf { it.isNotBlank() }
                Text(
                    text = buildString {
                        append(r.title ?: r.id ?: "-")
                        append(" · ")
                        append(r.date ?: "-")
                        append(" · ")
                        append(statusEs ?: "-")
                        if (!extra.isNullOrBlank()) append(" · $extra")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
                r.media.forEach { m ->
                    val formatEs = m.format?.lowercase()?.let { f ->
                        when {
                            f.contains("vinyl") -> "Vinilo"
                            f.contains("cd") -> "CD"
                            f.contains("digital") -> "Digital"
                            else -> m.format
                        }
                    } ?: m.format ?: "?"
                    val formatLine = "$formatEs · ${m.trackCount} pistas"
                    Text(
                        text = "  $formatLine",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth().padding(top = 1.dp),
                        textAlign = TextAlign.Center
                    )
                    m.tracks.take(5).forEach { t ->
                        Text(
                            text = "    ${t.position ?: "-"}. ${t.title ?: "-"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.fillMaxWidth().padding(top = 1.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (m.tracks.size > 5) {
                        Text(
                            text = "    ... y ${m.tracks.size - 5} pistas más",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.fillMaxWidth().padding(top = 1.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            if (detail.releases.size > 15) {
                Text(
                    text = "... y ${detail.releases.size - 15} más",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        if (detail.aliases.isNotEmpty()) {
            Text(
                text = "ALIAS: ${detail.aliases.take(10).joinToString(", ")}${if (detail.aliases.size > 10) "..." else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        if (detail.relations.isNotEmpty()) {
            Text(
                text = "RELACIONES (${detail.relations.size}):",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                textAlign = TextAlign.Center
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
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
            if (detail.relations.size > 10) {
                Text(
                    text = "... y ${detail.relations.size - 10} más",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddToCollection,
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text("AÑADIR A LA COLECCIÓN")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("VOLVER")
        }
    }
}

@Composable
private fun FallbackDetailContent(
    title: String,
    artistName: String,
    coverUrl: String,
    detailError: String? = null,
    onRetry: (() -> Unit)? = null,
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
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
        if (!detailError.isNullOrBlank()) {
            Text(
                text = detailError,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
            onRetry?.let { retry ->
                OutlinedButton(
                    onClick = retry,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("REINTENTAR")
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onAddToCollection,
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text("AÑADIR A LA COLECCIÓN")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
            Text("VOLVER")
        }
    }
}

@Composable
private fun CollectionPickerDialog(
    collections: List<CollectionWithCount>,
    onSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Añadir a colección", style = MaterialTheme.typography.titleLarge)
                if (collections.isEmpty()) {
                    Text(
                        "No tienes colecciones. Crea una en la pestaña Colecciones.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(collections) { c ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(c.id) }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(c.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${c.albumCount} álbum(s)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                }
            }
        }
    }
}
