package com.singaludra.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.singaludra.data.remote.network.ApiService
import com.singaludra.data.remote.response.ReviewResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieReviewPagingSource @Inject constructor(private val apiService: ApiService, private val movieId:Int) :
    PagingSource<Int, ReviewResponse>() {

    override fun getRefreshKey(state: PagingState<Int, ReviewResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewResponse> {
        return try {
            val nextPage = params.key ?: 1
            val movieList = apiService.getMovieReviews(movieId, nextPage)
            LoadResult.Page(
                data = movieList.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey =  if (movieList.results.isNotEmpty()) movieList.page + 1 else  null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (httpException: HttpException) {
            return LoadResult.Error(httpException)
        }
    }
}