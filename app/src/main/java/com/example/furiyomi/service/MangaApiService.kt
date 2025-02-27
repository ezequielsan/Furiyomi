package com.example.furiyomi.service

import com.example.furiyomi.model.MangaResponse
import com.example.furiyomi.model.TagResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApiService {
    @GET("manga")
    suspend fun getManga(
        @Query("title") title: String? = null,
        @Query("includedTags[]") tags: List<String>? = null,
        @Query("publicationDemographic[]") demographic: List<String>? = null,
        @Query("status[]") status: List<String>? = null,
        @Query("contentRating[]") contentRating: List<String>? = null,
        @Query("ids[]") ids: List<String>? = null,
        @Query("order[followedCount]") order: String? = null,

        // Reference expansion: Pegamos autor, artista e cover
        @Query("includes[]") includes: List<String> = listOf("author", "artist", "cover_art"),

        @Query("limit") limit: Int? = 10,
        @Query("offset") offset: Int? = 0
    ): MangaResponse

    @GET("manga/tag")
    suspend fun getTags(): TagResponse
}