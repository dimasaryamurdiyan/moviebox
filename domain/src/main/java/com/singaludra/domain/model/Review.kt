package com.singaludra.domain.model

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val url: String
)
