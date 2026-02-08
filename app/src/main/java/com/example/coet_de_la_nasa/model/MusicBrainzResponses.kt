package com.example.coet_de_la_nasa.model

import com.google.gson.annotations.SerializedName

data class ReleaseGroupSearchResponse(
    @SerializedName("release-groups") val releaseGroups: List<ReleaseGroup> = emptyList(),
    val count: Int = 0,
    val offset: Int = 0
)

data class ReleaseGroup(
    val id: String,
    val title: String,
    @SerializedName("primary-type") val primaryType: String? = null,
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit> = emptyList()
)

data class ArtistCredit(
    val name: String? = null,
    val artist: Artist? = null
)

data class Artist(
    val id: String,
    val name: String
)
