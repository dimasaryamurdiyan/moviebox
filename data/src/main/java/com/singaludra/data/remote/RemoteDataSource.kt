package com.singaludra.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.singaludra.data.remote.network.ApiResponse
import com.singaludra.data.remote.network.ApiService
import com.singaludra.data.remote.paging.MovieReviewPagingSource
import com.singaludra.data.remote.response.DetailMovieResponse
import com.singaludra.data.remote.response.GenreResponse
import com.singaludra.data.remote.response.MovieResponse
import com.singaludra.data.remote.response.ReviewResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


const val NETWORK_PAGE_SIZE = 1
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
): IRemoteDataSource {
    override fun getMovies(): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val result = apiService.getPopularMovie()
                if(result.results.isNotEmpty()) {
                    emit(ApiResponse.Success(result.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getMovieGenre(): Flow<ApiResponse<GenreResponse>> {
        return flow {
            try {
                val result = apiService.getGenreMovie()
                emit(ApiResponse.Success(result))
            } catch (e : Exception){
                emit(ApiResponse.Error(e.parse()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getDetailMovie(id: Int): Flow<ApiResponse<DetailMovieResponse>> {
        return flow {
            try {
                val result = apiService.getDetailMovie(id)
                emit(ApiResponse.Success(result))
            } catch (e : Exception){
                emit(ApiResponse.Error(e.parse()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getMovieReviews(id: Int): Flow<PagingData<ReviewResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieReviewPagingSource(apiService, id)
            }
        ).flow
    }

    private fun Exception.parse(): String {
        return when(this){
            is IOException -> {
                "You have no connection!"
            }

            is HttpException -> {
                if(this.code() == 422){
                    "Invalid Data"
                } else {
                    "Invalid request"
                }
            }

            else -> {
                this.message.toString()
            }
        }
    }

}