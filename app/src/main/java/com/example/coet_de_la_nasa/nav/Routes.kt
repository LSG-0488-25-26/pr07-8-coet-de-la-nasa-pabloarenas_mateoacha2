package com.example.coet_de_la_nasa.nav

sealed class Routes(val route: String) {
    object DogList : Routes("dog_list")

    object DogDetail : Routes("dog_detail/{breed}/{imageUrl}") {
        fun createRoute(breed: String, encodedImageUrl: String): String {
            return "dog_detail/$breed/$encodedImageUrl"
        }
    }
}

