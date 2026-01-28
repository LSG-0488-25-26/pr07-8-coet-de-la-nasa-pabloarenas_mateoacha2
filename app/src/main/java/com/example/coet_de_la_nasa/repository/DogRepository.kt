package com.example.coet_de_la_nasa.repository

import com.example.coet_de_la_nasa.api.DogCeoApi
import com.example.coet_de_la_nasa.model.DogItemUi
import com.example.coet_de_la_nasa.util.breedFromDogCeoUrl

class DogRepository(
    private val api: DogCeoApi = DogCeoApi.create()
) {
    suspend fun getDogFeed(count: Int = 30): Result<List<DogItemUi>> {
        return try {
            val response = api.getRandomImages(count)
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }

            val body = response.body()
                ?: return Result.failure(IllegalStateException("Empty body"))

            val items = body.images
                .filter { it.isNotBlank() }
                .map { url ->
                    DogItemUi(
                        breed = breedFromDogCeoUrl(url),
                        imageUrl = url
                    )
                }

            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomBreedImage(breed: String): Result<String> {
        return try {
            val response = api.getRandomImageByBreed(breed.lowercase())
            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException("HTTP ${response.code()}"))
            }

            val body = response.body()
                ?: return Result.failure(IllegalStateException("Empty body"))

            Result.success(body.imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

