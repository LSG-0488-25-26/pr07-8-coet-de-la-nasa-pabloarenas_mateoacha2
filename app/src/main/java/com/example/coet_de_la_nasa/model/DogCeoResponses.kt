package com.example.coet_de_la_nasa.model

import com.google.gson.annotations.SerializedName

data class DogImageListResponse(
    @SerializedName("message") val images: List<String>,
    val status: String
)

data class DogRandomImageResponse(
    @SerializedName("message") val imageUrl: String,
    val status: String
)

