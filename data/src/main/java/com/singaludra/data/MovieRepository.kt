package com.singaludra.data

import androidx.paging.PagingData
import androidx.paging.map
import com.singaludra.data.local.entity.MovieEntity
import com.singaludra.data.local.entity.mapToDomain
import com.singaludra.data.local.room.dao.MovieDao
import com.singaludra.data.remote.IRemoteDataSource
import com.singaludra.data.remote.network.ApiResponse
import com.singaludra.data.remote.response.DetailMovieResponse
import com.singaludra.data.remote.response.GenreResponse
import com.singaludra.data.remote.response.MovieResponse
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.model.Review
import com.singaludra.domain.repository.IMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: IRemoteDataSource,
    private val movieDao: MovieDao
) : IMovieRepository {
    override fun getMovies(): Flow<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<Movie>> {
                return movieDao.getAllMovie().map { movies ->
                    movies.map {
                        it.mapToDomain()
                    }
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovies()
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movies = MovieResponse.mapToEntity(data)
                movieDao.insertMovie(movies)
            }
        }.asFlow()
    }

    override fun getMovieGenre(): Flow<Resource<List<Movie.Genre>>> {
        return object : RepositoryLoader<GenreResponse, List<Movie.Genre>>(){
            override suspend fun createCall(): Flow<ApiResponse<GenreResponse>> {
                return remoteDataSource.getMovieGenre()
            }

            override fun mapApiResponseToDomain(data: GenreResponse): List<Movie.Genre> {
                return data.genres.map {
                    it.mapToDomain()
                }
            }
        }.asFlow()
    }

    override fun getMovieReviews(id: Int): Flow<PagingData<Review>> {
        return remoteDataSource.getMovieReviews(id)
            .map { pagingData ->
                pagingData.map { movieReview ->
                    movieReview.mapToDomain()
                }
            }
    }

    override fun getDetailMovie(id: Int): Flow<Resource<Movie>> {
        return object : RepositoryLoader<DetailMovieResponse, Movie>(){
            override suspend fun createCall(): Flow<ApiResponse<DetailMovieResponse>> {
                return remoteDataSource.getDetailMovie(id)
            }

            override fun mapApiResponseToDomain(data: DetailMovieResponse): Movie {
                return data.mapToDomain()
            }
        }.asFlow()
    }

    override suspend fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val movieEntity = MovieEntity.mapFromDomain(movie)
        movieEntity.isFavorite = state
        movieDao.updateFavoriteMovie(movieEntity)
    }

    override fun getFavoriteMovies(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = movieDao.getFavoriteMovie().first().map {
                    it.mapToDomain()
                }
                emit(Resource.Success(result))
            } catch (e: Exception){
                emit(Resource.Error(e.message ?: "undefined exception"))
            }
        }
    }

}