package com.example.coet_de_la_nasa.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Acepta número o string en JSON (MusicBrainz a veces devuelve "position" o "track-count" como string).
 */
class FlexibleIntDeserializer : JsonDeserializer<Int> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int {
        if (json.isJsonNull) return 0
        return when {
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asInt
            json.isJsonPrimitive && json.asJsonPrimitive.isString -> json.asString.toIntOrNull() ?: 0
            else -> 0
        }
    }
}
