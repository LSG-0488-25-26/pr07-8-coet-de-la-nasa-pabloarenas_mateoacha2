package com.example.coet_de_la_nasa.api

import com.example.coet_de_la_nasa.model.LeagueDetailResponse
import com.example.coet_de_la_nasa.model.LeagueListResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FootballApi::class.java)
        }
    }
}
