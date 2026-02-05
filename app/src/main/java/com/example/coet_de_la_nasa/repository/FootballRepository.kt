package com.example.coet_de_la_nasa.repository

import com.example.coet_de_la_nasa.api.FootballApi
import com.example.coet_de_la_nasa.model.LeagueItemUi

class FootballRepository(
    private val api: FootballApi = FootballApi.create()
) {
    suspend fun getLeagues(): Result<List<LeagueItemUi>> {
        return try {
            val response = api.getAllLeagues()
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }

            val body = response.body()
                ?: return Result.failure(IllegalStateException("Empty body"))

            val items = body.data.map { league ->
                LeagueItemUi(
                    id = league.id,
                    name = league.name,
                    abbreviation = league.abbr,
                    logoUrl = league.logos.light
                )
            }

            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLeagueDetail(id: String): Result<LeagueItemUi> {
        return try {
            val response = api.getLeagueDetail(id)
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }

            val body = response.body()
                ?: return Result.failure(IllegalStateException("Empty body"))

            val league = LeagueItemUi(
                id = body.data.id,
                name = body.data.name,
                abbreviation = body.data.abbr,
                logoUrl = body.data.logos.light
            )

            Result.success(league)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
