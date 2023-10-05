package com.singaludra.movieflix.feature.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.repository.IMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieUiState{
    data class Success(val movie: List<Movie>): MovieUiState
    data class Error(val exception: String): MovieUiState

    object Loading: MovieUiState
}
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: IMovieRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            movieRepository.getMovies().collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        if (result.data?.isNotEmpty() == true){
                            _uiState.value = MovieUiState.Success(result.data!!)
                        } else {
                            _uiState.value = MovieUiState.Success(emptyList())
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = MovieUiState.Error(result.message ?: "undefined")
                    }
                    is Resource.Loading -> {
                        _uiState.value = MovieUiState.Loading
                    }
                }
            }
        }
    }

    fun searchMovies(name: String){
        viewModelScope.launch {
            movieRepository.searchMovies(name).collect{ result ->
                when(result) {
                    is Resource.Success -> {
                        if (result.data?.isNotEmpty() == true){
                            _uiState.value = MovieUiState.Success(result.data!!)
                        } else {
                            _uiState.value = MovieUiState.Success(emptyList())
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = MovieUiState.Error(result.message ?: "undefined")
                    }
                    is Resource.Loading -> {
                        _uiState.value = MovieUiState.Loading
                    }
                }
            }
        }
    }
}