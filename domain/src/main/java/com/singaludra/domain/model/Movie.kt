package com.singaludra.domain.model

data class Movie(
    val id: Int = 0,
    val isAdultOnly: Boolean? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val image: String,
    val backdropImage: String? = null,
    val title: String,
    val overview: String,
    val releaseDate: String? = null,
    val runtime: Int? = null,
    val originalLanguage: String? = null,
    val genres: List<Genre>? = null,
    val isFavorite: Boolean = false
) {
    class Genre(
        val id: Int,
        val name: String
    )
}
