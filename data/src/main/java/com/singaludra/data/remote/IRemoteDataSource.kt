package com.singaludra.data.remote

import androidx.paging.PagingData
import com.singaludra.data.remote.network.ApiResponse
import com.singaludra.data.remote.response.DetailMovieResponse
import com.singaludra.data.remote.response.GenreResponse
import com.singaludra.data.remote.response.MovieResponse
import com.singaludra.data.remote.response.ReviewResponse
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {
    fun getMovies(): Flow<ApiResponse<List<MovieResponse>>>
    fun getMovieGenre(): Flow<ApiResponse<GenreResponse>>
    fun getDetailMovie(id: Int): Flow<ApiResponse<DetailMovieResponse>>
    fun getMovieReviews(id: Int): Flow<PagingData<ReviewResponse>>
}