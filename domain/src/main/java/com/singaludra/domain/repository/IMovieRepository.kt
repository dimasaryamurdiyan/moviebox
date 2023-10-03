package com.singaludra.domain.repository

import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getMovies(): Flow<Resource<List<Movie>>>
    fun getMovieReviews(id: Int): Flow<Resource<List<Review>>>
    fun getDetailMovie(id: Int): Flow<Resource<Movie>>
}