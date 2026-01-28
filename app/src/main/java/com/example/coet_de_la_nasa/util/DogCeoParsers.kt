package com.example.coet_de_la_nasa.util

/**
 * dog.ceo devuelve URLs tipo:
 * https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg
 *
 * De ahí sacamos el "breed" como el primer bloque antes del '-' (hound)
 * y lo mostramos capitalizado.
 */
fun breedFromDogCeoUrl(url: String): String {
    val marker = "/breeds/"
    val idx = url.indexOf(marker)
    if (idx == -1) return "Unknown"

    val after = url.substring(idx + marker.length) // e.g. "hound-afghan/n020..."
    val folder = after.substringBefore("/")         // e.g. "hound-afghan"
    val rawBreed = folder.substringBefore("-")      // e.g. "hound"

    return rawBreed
        .replace("_", " ")
        .replace("-", " ")
        .trim()
        .replaceFirstChar { it.uppercase() }
}

fun breedToApiPath(breed: String): String {
    // dog.ceo espera paths en minúsculas, sin espacios. Ej: "German Shepherd" -> "german shepherd"
    return breed.trim().lowercase()
}

