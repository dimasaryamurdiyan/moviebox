package com.singaludra.data.remote.response

import com.google.gson.annotations.SerializedName
import com.singaludra.data.local.entity.MovieEntity
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
        fun mapToDomain(movieResponse: List<MovieResponse>): List<Movie> {
            return movieResponse.map {
                Movie(
                    id = it.id,
                    title = it.originalTitle ?: it.title,
                    overview = it.overview,
                    image = it.posterPath,
                )
            }
        }

        fun mapToEntity(movieResponse: List<MovieResponse>): List<MovieEntity> {
            return movieResponse.map {
                MovieEntity(
                    id = it.id,
                    originalTitle = it.originalTitle ?: it.title,
                    overview = it.overview,
                    posterPath = it.posterPath,
                )
            }
        }
    }
}