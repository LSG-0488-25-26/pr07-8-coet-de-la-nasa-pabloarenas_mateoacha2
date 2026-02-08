package com.example.coet_de_la_nasa.model

import com.google.gson.annotations.JsonAdapter
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
    @SerializedName("secondary-types")
    @JsonAdapter(SecondaryTypesDeserializer::class)
    val secondaryTypes: List<String>? = null,
    @SerializedName("first-release-date") val firstReleaseDate: String? = null,
    val disambiguation: String? = null,
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit> = emptyList(),
    val tags: List<Tag>? = null,
    val genres: List<Genre>? = null,
    val rating: Rating? = null,
    val releases: List<Release>? = null,
    val aliases: List<Alias>? = null,
    @JsonAdapter(AnnotationDeserializer::class)
    val annotation: Annotation? = null,
    val relations: List<Relation>? = null
)

data class Annotation(
    val text: String? = null
)

data class Release(
    val id: String? = null,
    val title: String? = null,
    val date: String? = null,
    @SerializedName("status-id") val statusId: String? = null,
    val status: String? = null
)

data class Alias(
    val name: String? = null,
    val locale: String? = null,
    @SerializedName("primary") val primary: String? = null
)

data class Relation(
    val type: String? = null,
    @SerializedName("type-id") val typeId: String? = null,
    val direction: String? = null,
    @SerializedName("target-type") val targetType: String? = null,
    val url: RelationUrl? = null,
    val attributes: List<String>? = null
)

data class RelationUrl(
    val id: String? = null,
    val resource: String? = null
)

data class ArtistCredit(
    val name: String? = null,
    val artist: Artist? = null
)

data class Artist(
    val id: String,
    val name: String
)

data class Tag(
    val name: String? = null,
    val count: Int = 0
)

data class Genre(
    val id: String? = null,
    val name: String? = null,
    val count: Int = 0
)

data class Rating(
    val value: Float? = null,
    @SerializedName("votes-count") val votesCount: Int = 0
)
