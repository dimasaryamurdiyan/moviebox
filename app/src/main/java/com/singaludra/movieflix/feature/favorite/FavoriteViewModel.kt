package com.singaludra.movieflix.feature.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.repository.IMovieRepository
import com.singaludra.movieflix.feature.movies.MovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FavriteMovieUiState{
    data class Success(val movie: List<Movie>): FavriteMovieUiState
    data class Error(val exception: String): FavriteMovieUiState

    object Loading: FavriteMovieUiState
}
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: IMovieRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<FavriteMovieUiState>(FavriteMovieUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getMovies()
    }
    fun getMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovies().collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        if (result.data?.isNotEmpty() == true){
                            _uiState.value = FavriteMovieUiState.Success(result.data!!)
                        } else {
                            _uiState.value = FavriteMovieUiState.Success(emptyList())
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = FavriteMovieUiState.Error(result.message ?: "undefined")
                    }
                    is Resource.Loading -> {
                        _uiState.value = FavriteMovieUiState.Loading
                    }
                }
            }
        }
    }
}