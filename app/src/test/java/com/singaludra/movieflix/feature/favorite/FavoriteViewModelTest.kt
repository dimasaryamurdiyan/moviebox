package com.singaludra.movieflix.feature.favorite

import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.repository.IMovieRepository
import com.singaludra.movieflix.TestCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class FavoriteViewModelTest{
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var movieRepository: IMovieRepository

    // Create an instance of the ViewModel
    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = FavoriteViewModel(movieRepository)
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
        Mockito.`when`(movieRepository.getFavoriteMovies()).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.getMovies()

        val uiState = viewModel.uiState.first()
        TestCase.assertTrue(uiState is FavriteMovieUiState.Success)
        TestCase.assertEquals((uiState as FavriteMovieUiState.Success).movie, movies)
    }

    @Test
    fun `test getMovies empty list`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val emptyMovies = emptyList<Movie>()
        val successResource = Resource.Success(emptyMovies)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.getFavoriteMovies()).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.getMovies()

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        TestCase.assertTrue(uiState is FavriteMovieUiState.Success)
        TestCase.assertEquals((uiState as FavriteMovieUiState.Success).movie, emptyMovies)
    }

    @Test
    fun `test getMovies error`() = testCoroutineRule.runBlockingTest {
        // Define the error message
        val errorMessage = "An error occurred"
        val errorResource = Resource.Error<List<Movie>>(errorMessage)

        // Mock the behavior of movieRepository.getMovies()
        Mockito.`when`(movieRepository.getFavoriteMovies()).thenReturn(flowOf(errorResource))

        // Call the ViewModel method
        viewModel.getMovies()

        // Verify that the UI state is updated correctly
        val uiState = viewModel.uiState.first()
        TestCase.assertTrue(uiState is FavriteMovieUiState.Error)
        TestCase.assertEquals((uiState as FavriteMovieUiState.Error).exception, errorMessage)
    }
}