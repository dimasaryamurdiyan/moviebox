package com.singaludra.domain.repository

import androidx.paging.PagingData
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getMovies(): Flow<Resource<List<Movie>>>
    fun getMovieGenre(): Flow<Resource<List<Movie.Genre>>>
    fun getMovieReviews(id: Int): Flow<PagingData<Review>>
    fun getDetailMovie(id: Int): Flow<Resource<Movie>>
    suspend fun setFavoriteMovie(movie: Movie, state: Boolean)
    fun getFavoriteMovies(): Flow<Resource<List<Movie>>>
    fun searchMovies(name: String): Flow<Resource<List<Movie>>>
}