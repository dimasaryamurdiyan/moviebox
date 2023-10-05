package com.singaludra.movieflix.feature.movies

import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.repository.IMovieRepository
import com.singaludra.movieflix.TestCoroutineRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var movieRepository: IMovieRepository

    // Create an instance of the ViewModel
    private lateinit var viewModel: MovieViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MovieViewModel(movieRepository)
    }

    @Test
    fun `test getMovies success`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val movies = listOf(
            Movie(
                image = "",
                title = "test",
                overview = "testt"
            )
        )
        val successResource = Resource.Success(movies)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.getMovies()).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.getMovies()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Success)
        assertEquals((uiState as MovieUiState.Success).movie, movies)
    }

    @Test
    fun `test getMovies empty list`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val emptyMovies = emptyList<Movie>()
        val successResource = Resource.Success(emptyMovies)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.getMovies()).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.getMovies()

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Success)
        assertEquals((uiState as MovieUiState.Success).movie, emptyMovies)
    }

    @Test
    fun `test getMovies error`() = testCoroutineRule.runBlockingTest {
        // Define the error message
        val errorMessage = "An error occurred"
        val errorResource = Resource.Error<List<Movie>>(errorMessage)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.getMovies()).thenReturn(flowOf(errorResource))

        // Call the ViewModel method
        viewModel.getMovies()

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Error)
        assertEquals((uiState as MovieUiState.Error).exception, errorMessage)
    }

    @Test
    fun `test searchMovies success`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val movieName ="batman"
        val movies = listOf(
            Movie(
                image = "",
                title = "test",
                overview = "testt"
            )
        )
        val successResource = Resource.Success(movies)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.searchMovies(movieName)).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.searchMovies(movieName)

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Success)
        assertEquals((uiState as MovieUiState.Success).movie, movies)
    }

    @Test
    fun `test searchMovies empty list`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val movieName ="batman"
        val emptyMovies = emptyList<Movie>()
        val successResource = Resource.Success(emptyMovies)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.searchMovies(movieName)).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.searchMovies(movieName)

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Success)
        assertEquals((uiState as MovieUiState.Success).movie, emptyMovies)
    }

    @Test
    fun `test searchMovies error`() = testCoroutineRule.runBlockingTest {
        // Define the error message
        val movieName ="batman"
        val errorMessage = "An error occurred"
        val errorResource = Resource.Error<List<Movie>>(errorMessage)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.searchMovies(movieName)).thenReturn(flowOf(errorResource))

        // Call the ViewModel method
        viewModel.searchMovies(movieName)

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is MovieUiState.Error)
        assertEquals((uiState as MovieUiState.Error).exception, errorMessage)
    }
}