package com.example.coet_de_la_nasa.api

import com.example.coet_de_la_nasa.model.LeagueDetailResponse
import com.example.coet_de_la_nasa.model.LeagueListResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballApi {
    @GET("leagues")
    suspend fun getAllLeagues(): Response<LeagueListResponse>

    @GET("leagues/{id}")
    suspend fun getLeagueDetail(@Path("id") id: String): Response<LeagueDetailResponse>

    companion object {
        private const val BASE_URL = "https://api-football-standings.azharimm.site/"

        fun create(): FootballApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FootballApi::class.java)
        }
    }
}
