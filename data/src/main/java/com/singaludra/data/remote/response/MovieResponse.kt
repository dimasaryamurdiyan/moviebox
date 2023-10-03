package com.singaludra.data.remote.response

import com.google.gson.annotations.SerializedName
import com.singaludra.domain.model.Movie

data class MovieResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("original_title")
    var originalTitle: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("title")
    var title: String,
){
    companion object {
        fun mapToDomain(movieResponse: MovieResponse): Movie {
            return Movie(
                id = movieResponse.id,
                title = movieResponse.originalTitle ?: movieResponse.title,
                overview = movieResponse.overview,
                image = movieResponse.posterPath,
            )
        }
    }
}