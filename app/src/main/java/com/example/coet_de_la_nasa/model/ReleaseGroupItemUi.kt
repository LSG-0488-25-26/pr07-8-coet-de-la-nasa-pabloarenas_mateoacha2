package com.example.coet_de_la_nasa.model

/**
 * Item para la UI: álbum (release-group) de MusicBrainz.
 * coverUrl usa Cover Art Archive: https://coverartarchive.org/release-group/{mbid}/front-500.jpg
 */
data class ReleaseGroupItemUi(
    val mbid: String,
    val title: String,
    val artistName: String,
    val coverUrl: String,
    val primaryType: String? = null,
    val firstReleaseDate: String? = null,
    val disambiguation: String? = null,
    val secondaryTypes: List<String>? = null
)

/**
 * Resum d’una release per la UI.
 */
/** Resum d'una pista (track) per la UI. */
data class TrackSummaryUi(
    val title: String?,
    val position: Int?,
    val length: Long? = null
)

/** Resum d'un suport (media: CD, vinil, etc.) per la UI. */
data class MediaSummaryUi(
    val format: String?,
    val trackCount: Int,
    val tracks: List<TrackSummaryUi> = emptyList()
)

data class ReleaseSummaryUi(
    val id: String?,
    val title: String?,
    val date: String?,
    val status: String?,
    val country: String? = null,
    val barcode: String? = null,
    val media: List<MediaSummaryUi> = emptyList()
)

/**
 * Resum d’una relació per la UI.
 */
data class RelationSummaryUi(
    val type: String?,
    val direction: String?,
    val targetType: String?,
    val url: String?
)

/**
 * Detall complet per la pantalla de detall (lookup amb tot inc=).
 */
data class ReleaseGroupDetailUi(
    val mbid: String,
    val title: String,
    val artistName: String,
    val coverUrl: String,
    val primaryType: String? = null,
    val firstReleaseDate: String? = null,
    val disambiguation: String? = null,
    val secondaryTypes: List<String>? = null,
    val tags: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val ratingValue: Float? = null,
    val ratingVotes: Int = 0,
    val releases: List<ReleaseSummaryUi> = emptyList(),
    val aliases: List<String> = emptyList(),
    val annotation: String? = null,
    val relations: List<RelationSummaryUi> = emptyList()
)
