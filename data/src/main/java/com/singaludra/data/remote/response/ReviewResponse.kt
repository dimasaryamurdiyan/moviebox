package com.singaludra.data.remote.response

import com.google.gson.annotations.SerializedName
import com.singaludra.domain.model.Review

data class ReviewResponse(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
) {
    fun mapToDomain(): Review {
        return Review(
            author, content, createdAt, id, updatedAt, url
        )
    }
}