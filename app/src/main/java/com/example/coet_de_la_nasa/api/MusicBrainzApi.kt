package com.example.coet_de_la_nasa.api

import com.example.coet_de_la_nasa.model.ReleaseGroupSearchResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * MusicBrainz API (https://musicbrainz.org/doc/MusicBrainz_API).
 * - Sin API key; obligatorio User-Agent.
 * - Límite: 1 petición por segundo.
 */
interface MusicBrainzApi {

    @GET("release-group")
    suspend fun searchReleaseGroups(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<ReleaseGroupSearchResponse>

    companion object {
        private const val BASE_URL = "https://musicbrainz.org/ws/2/"

        fun create(): MusicBrainzApi {
            val userAgentInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "CoetDeLaNasa/1.0 ( Android )")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(userAgentInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MusicBrainzApi::class.java)
        }
    }
}
