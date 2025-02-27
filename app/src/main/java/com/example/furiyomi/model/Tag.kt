package com.example.furiyomi.model

import com.google.gson.annotations.SerializedName

data class Tag(
    val id: String,
    val attributes: TagAttributes
)

data class TagAttributes(
    val name: Map<String, String>,
    val group: String
) {
    fun getName(): String {
        return name["en"] ?: "Unknown Tag"
    }
}

data class TagResponse(
    @SerializedName("data") val tags: List<Tag>
)
