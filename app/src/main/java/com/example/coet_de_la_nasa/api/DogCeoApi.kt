package com.example.coet_de_la_nasa.api

import com.example.coet_de_la_nasa.model.DogImageListResponse
import com.example.coet_de_la_nasa.model.DogRandomImageResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DogCeoApi {
    @GET("api/breeds/image/random/{count}")
    suspend fun getRandomImages(@Path("count") count: Int): Response<DogImageListResponse>

    @GET("api/breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(@Path("breed") breed: String): Response<DogRandomImageResponse>

    companion object {
        private const val BASE_URL = "https://dog.ceo/"

        fun create(): DogCeoApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DogCeoApi::class.java)
        }
    }
}
