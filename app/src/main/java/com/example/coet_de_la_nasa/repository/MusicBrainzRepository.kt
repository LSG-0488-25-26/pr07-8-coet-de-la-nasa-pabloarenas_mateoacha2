package com.example.coet_de_la_nasa.repository

import com.example.coet_de_la_nasa.api.MusicBrainzApi
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi

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
            val items = body.releaseGroups.map { rg ->
                val artistName = rg.artistCredit
                    .mapNotNull { it.artist?.name ?: it.name }
                    .joinToString(", ")
                    .ifBlank { "Unknown" }
                ReleaseGroupItemUi(
                    mbid = rg.id,
                    title = rg.title,
                    artistName = artistName,
                    coverUrl = "$COVER_ART_BASE${rg.id}/front-500.jpg"
                )
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
