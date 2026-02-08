package com.example.coet_de_la_nasa.repository

import com.example.coet_de_la_nasa.api.MusicBrainzApi
import com.example.coet_de_la_nasa.model.ReleaseGroup
import com.example.coet_de_la_nasa.model.ReleaseGroupDetailUi
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi
import com.example.coet_de_la_nasa.model.ReleaseSummaryUi
import com.example.coet_de_la_nasa.model.RelationSummaryUi

/**
 * Portadas desde Cover Art Archive: https://coverartarchive.org/release-group/{mbid}/front-500.jpg
 */
private const val COVER_ART_BASE = "https://coverartarchive.org/release-group/"

class MusicBrainzRepository(
    private val api: MusicBrainzApi = MusicBrainzApi.create()
) {

    suspend fun searchReleaseGroups(
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<ReleaseGroupItemUi>> {
        return try {
            if (query.isBlank()) {
                return Result.success(emptyList())
            }
            val response = api.searchReleaseGroups(query = query, limit = limit, offset = offset)
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }
            val body = response.body() ?: return Result.failure(IllegalStateException("Empty body"))
            val items = body.releaseGroups.map { rg -> rg.toItemUi() }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun lookupReleaseGroup(mbid: String): Result<ReleaseGroupDetailUi> {
        return try {
            val response = api.lookupReleaseGroup(mbid = mbid)
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }
            val rg = response.body() ?: return Result.failure(IllegalStateException("Empty body"))
            Result.success(rg.toDetailUi())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun ReleaseGroup.toItemUi(): ReleaseGroupItemUi {
    val artistName = artistCredit
        .mapNotNull { it.artist?.name ?: it.name }
        .joinToString(", ")
        .ifBlank { "Unknown" }
    return ReleaseGroupItemUi(
        mbid = id,
        title = title,
        artistName = artistName,
        coverUrl = "$COVER_ART_BASE$id/front-500.jpg",
        primaryType = primaryType,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        secondaryTypes = secondaryTypes
    )
}

private fun ReleaseGroup.toDetailUi(): ReleaseGroupDetailUi {
    val artistName = artistCredit
        .mapNotNull { it.artist?.name ?: it.name }
        .joinToString(", ")
        .ifBlank { "Unknown" }
    return ReleaseGroupDetailUi(
        mbid = id,
        title = title,
        artistName = artistName,
        coverUrl = "$COVER_ART_BASE$id/front-500.jpg",
        primaryType = primaryType,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        secondaryTypes = secondaryTypes,
        tags = tags?.mapNotNull { it.name }.orEmpty(),
        genres = genres?.mapNotNull { it.name }.orEmpty(),
        ratingValue = rating?.value,
        ratingVotes = rating?.votesCount ?: 0,
        releases = releases?.map { r ->
            ReleaseSummaryUi(id = r.id, title = r.title, date = r.date, status = r.status)
        }.orEmpty(),
        aliases = aliases?.mapNotNull { it.name }.orEmpty(),
        annotation = annotation?.text,
        relations = relations?.map { r ->
            RelationSummaryUi(
                type = r.type,
                direction = r.direction,
                targetType = r.targetType,
                url = r.url?.resource
            )
        }.orEmpty()
    )
}
