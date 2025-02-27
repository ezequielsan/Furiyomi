package com.example.furiyomi.model

data class MangaFilters(
    val title: String?,
    val demographic: List<String>?,
    val contentRating: List<String>?,
    val status: List<String>?
)