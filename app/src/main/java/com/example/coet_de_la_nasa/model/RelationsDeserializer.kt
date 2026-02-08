package com.example.coet_de_la_nasa.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * MusicBrainz pot retornar "relations" com a array o com a objecte (mapa tipus -> llista).
 * Aquest deserialitzador accepta els dos i no llança mai.
 */
class RelationsDeserializer : JsonDeserializer<List<Relation>?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<Relation>? {
        if (json.isJsonNull) return null
        return try {
            when {
                json.isJsonArray -> json.asJsonArray.mapNotNull { parseRelation(it, context) }
                json.isJsonObject -> json.asJsonObject.entrySet()
                    .flatMap { (_, value) -> if (value.isJsonArray) value.asJsonArray.mapNotNull { parseRelation(it, context) } else emptyList() }
                    .ifEmpty { null }
                else -> null
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseRelation(el: JsonElement, context: JsonDeserializationContext): Relation? =
        try {
            context.deserialize(el, Relation::class.java)
        } catch (_: Exception) { null }
}
