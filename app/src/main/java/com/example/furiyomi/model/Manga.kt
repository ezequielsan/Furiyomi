package com.example.furiyomi.model

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    val total: Int,
    val data: List<Manga>
)

data class Manga(
    val id: String,
    val attributes: MangaAttributes,
    val relationships: List<MangaRelationship>
) {
    /* companion object {
         fun fromResponse(response: MangaResponse): List<Manga> {
             return response.data.map { manga ->
                 val author = manga.relationships.author

                 Manga(
                     id = manga.id,
                     attributes = MangaAttributes(
                         title = manga.attributes.title,
                         description = manga.attributes.description,
                         tags = manga.attributes.tags,
                         lastVolume = manga.attributes.lastVolume,
                         lastChapter = manga.attributes.lastChapter,
                         publicationDemographic = manga.attributes.publicationDemographic,
                         status = manga.attributes.status,
                         year = manga.attributes.year,
                         contentRating = manga.attributes.contentRating,
                         createdAt = manga.attributes.createdAt,
                         updatedAt = manga.attributes.updatedAt,
                         language = manga.attributes.language
                     ),
                     relationships = MangaRelationships(
                         author = author
                     )
                 )
             }
         }
     }*/
}

data class MangaAttributes(
    val title: Map<String, String>,
    val description: Map<String, String>?,
    val tags: List<Tag>,
    val lastVolume: String,
    val lastChapter: String,
    val publicationDemographic: String,
    val status: String,
    val year: String,
    val contentRating: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("originalLanguage") val language: String
) {
    fun getTitle(): String {
        return title["en"] ?: title.values.firstOrNull() ?: "sem titulo"
    }

    fun getDescription(): String {
        return description?.get("en") ?: description?.values?.firstOrNull() ?: "sem descrição"
    }
}

sealed class MangaRelationship {
    abstract val id: String
    abstract val type: String

    data class Author(
        override val id: String,
        override val type: String = "author",
        val name: String?
    ) : MangaRelationship()

    data class Artist(
        override val id: String,
        override val type: String = "artist"
    ) : MangaRelationship()

    data class CoverArt(
        override val id: String,
        override val type: String = "cover_art",
        val fileName: String?,
        val volume: String?
    ) : MangaRelationship()

    data class Unknown(
        override val id: String,
        override val type: String
    ) : MangaRelationship()
}

data class MangaStatisticsResponse(
    val result: String,
    val statistics: Map<String, MangaStatistics>
)

data class MangaStatistics(
    val follows: Int,
    val rating: MangaRating
)

data class MangaRating(
    val average: Float,
    val bayesian: Float
)
