package com.singaludra.data.remote.response

import com.google.gson.annotations.SerializedName
import com.singaludra.domain.model.Movie

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<GenreItem>
) {
    data class GenreItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    ) {
        fun mapToDomain(): Movie.Genre {
            return Movie.Genre(this.id, this.name)
        }
    }
}