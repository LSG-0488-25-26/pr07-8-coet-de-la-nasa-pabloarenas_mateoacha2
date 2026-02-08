package com.example.coet_de_la_nasa.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Acepta número o string en JSON para Int? (p. ej. "position" en tracks).
 */
class FlexibleNullableIntDeserializer : JsonDeserializer<Int?> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int? {
        if (json.isJsonNull) return null
        return when {
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asInt
            json.isJsonPrimitive && json.asJsonPrimitive.isString -> json.asString.toIntOrNull()
            else -> null
        }
    }
}
