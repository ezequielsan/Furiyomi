package com.example.furiyomi.service

import com.example.furiyomi.model.MangaRelationship
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.mangadex.org/"

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(MangaRelationship::class.java, MangaRelationshipAdapter())
        .create()

    val api: MangaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MangaApiService::class.java)
    }
}