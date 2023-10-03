package com.singaludra.movieflix.domain.model

data class Movie(
    val id: Int = 0,
    val isAdultOnly: Boolean,
    val popularity: Double,
    val voteAverage: Double,
    val image: String,
    val backdropImage: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val runtime: Int,
    val originalLanguage: String,
    val genres: List<Genre>
) {
    class Genre(
        val id: Int,
        val name: String
    )
}
