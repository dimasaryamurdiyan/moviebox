package com.singaludra.data.remote.network

import com.singaludra.data.remote.response.DetailMovieResponse
import com.singaludra.data.remote.response.GenreResponse
import com.singaludra.data.remote.response.MovieResponse
import com.singaludra.data.remote.response.ResponseItems
import com.singaludra.data.remote.response.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovie(
    ): ResponseItems<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") id: Int
    ): DetailMovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): ResponseItems<ReviewResponse>

    @GET("genre/movie/list")
    suspend fun getGenreMovie(): GenreResponse

}