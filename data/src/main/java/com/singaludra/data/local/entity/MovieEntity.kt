package com.singaludra.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.singaludra.domain.model.Movie

@Entity(tableName = "movie")
data class MovieEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "originalTitle")
    val originalTitle: String,

    @ColumnInfo(name = "posterPath")
    val posterPath: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,

) {
    companion object {
        fun mapFromDomain(movie: Movie):  MovieEntity {
            return MovieEntity(
                id = movie.id,
                overview = movie.overview,
                originalTitle = movie.title,
                posterPath = movie.image,
            )
        }
    }
}