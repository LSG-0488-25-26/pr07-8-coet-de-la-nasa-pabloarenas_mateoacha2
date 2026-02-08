package com.example.coet_de_la_nasa.model

/**
 * Item para la UI: álbum (release-group) de MusicBrainz.
 * coverUrl usa Cover Art Archive: https://coverartarchive.org/release-group/{mbid}/front-500.jpg
 */
data class ReleaseGroupItemUi(
    val mbid: String,
    val title: String,
    val artistName: String,
    val coverUrl: String
)
