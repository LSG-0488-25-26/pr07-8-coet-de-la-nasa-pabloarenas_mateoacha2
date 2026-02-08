package com.example.coet_de_la_nasa.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * MusicBrainz pot retornar "annotation" com a string o com a objecte {"text": "..."}.
 */
class AnnotationDeserializer : JsonDeserializer<Annotation?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Annotation? {
        if (json.isJsonNull) return null
        return when {
            json.isJsonPrimitive && json.asJsonPrimitive.isString -> Annotation(text = json.asString)
            json.isJsonObject -> {
                val obj = json.asJsonObject
                val text = obj.get("text")?.takeIf { it.isJsonPrimitive }?.asString
                    ?: obj.get("content")?.takeIf { it.isJsonPrimitive }?.asString
                if (text != null) Annotation(text = text) else null
            }
            else -> null
        }
    }
}
