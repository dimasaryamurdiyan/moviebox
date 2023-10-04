package com.singaludra.movieflix.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.model.Review
import com.singaludra.domain.repository.IMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailMovieUiState{
    data class Success(val movie: Movie): DetailMovieUiState
    data class Error(val exception: String): DetailMovieUiState

    object Loading: DetailMovieUiState
}

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repository: IMovieRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<DetailMovieUiState>(DetailMovieUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            repository.getDetailMovie(id).collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        _uiState.value = DetailMovieUiState.Success(result.data!!)
                    }
                    is Resource.Error -> {
                        _uiState.value = DetailMovieUiState.Error(result.message ?: "undefined")
                    }
                    is Resource.Loading -> {
                        _uiState.value = DetailMovieUiState.Loading
                    }
                }
            }
        }
    }

    fun getMovieReview(id: Int): Flow<PagingData<Review>> {
        return repository.getMovieReviews(id)
            .cachedIn(viewModelScope)
    }

    fun setFavoriteGame(movie: Movie, newStatus:Boolean) =
        repository.setFavoriteMovie(movie, newStatus)
}