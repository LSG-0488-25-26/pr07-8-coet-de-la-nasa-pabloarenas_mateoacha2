package com.example.coet_de_la_nasa.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * MusicBrainz pot retornar "secondary-types" com a array de strings ("Live", "Compilation")
 * o com a array d'objectes {"id": "...", "name": "Live"}. Aquest deserialitzador accepta els dos.
 */
class SecondaryTypesDeserializer : JsonDeserializer<List<String>?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<String>? {
        if (json.isJsonNull) return null
        if (!json.isJsonArray) return null
        val arr = json.asJsonArray
        return arr.mapNotNull { element ->
            when {
                element.isJsonPrimitive && element.asJsonPrimitive.isString -> element.asString
                element.isJsonObject -> element.asJsonObject.get("name")?.takeIf { it.isJsonPrimitive }?.asString
                else -> null
            }
        }
    }
}
