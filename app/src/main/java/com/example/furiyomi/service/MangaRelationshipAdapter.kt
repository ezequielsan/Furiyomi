package com.example.furiyomi.service

import com.example.furiyomi.model.MangaRelationship
import com.google.gson.JsonDeserializer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import java.lang.reflect.Type

class MangaRelationshipAdapter : JsonDeserializer<MangaRelationship> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MangaRelationship {
        val jsonObject = json.asJsonObject
        val id = jsonObject.get("id").asString
        val type = jsonObject.get("type").asString

        return when (type) {
            "author" -> MangaRelationship.Author(
                id = id,
                name = jsonObject.getAsJsonObject("attributes")?.get("name")?.let {
                    if (it is JsonNull) null else it.asString
                }
            )
            "artist" -> MangaRelationship.Artist(id = id)
            "cover_art" -> MangaRelationship.CoverArt(
                id = id,
                fileName = jsonObject.getAsJsonObject("attributes")?.get("fileName")?.let {
                    if (it is JsonNull) null else it.asString
                },
                volume = jsonObject.getAsJsonObject("attributes")?.get("volume")?.let {
                    if (it is JsonNull) null else it.asString
                }

            )
            else -> MangaRelationship.Unknown(id = id, type = type)
            // else -> throw JsonParseException("Tipo desconhecido: $type")
        }
    }
}