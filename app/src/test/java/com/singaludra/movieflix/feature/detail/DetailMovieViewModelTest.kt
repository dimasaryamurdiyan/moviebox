package com.singaludra.movieflix.feature.detail

import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.singaludra.domain.Resource
import com.singaludra.domain.model.Movie
import com.singaludra.domain.model.Review
import com.singaludra.domain.repository.IMovieRepository
import com.singaludra.movieflix.TestCoroutineRule
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailMovieViewModelTest{
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var movieRepository: IMovieRepository

    // Create an instance of the ViewModel
    private lateinit var viewModel: DetailMovieViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = DetailMovieViewModel(movieRepository)
    }

    @Test
    fun `test getMovieDetail success`() = testCoroutineRule.runBlockingTest {
        // Define the test data
        val movieId = 1
        val movie = Movie(
            image = "",
            title = "test",
            overview = "testt"
        )
        val successResource = Resource.Success(movie)

        // Mock the behavior of repository.getDetailMovie()
        Mockito.`when`(movieRepository.getDetailMovie(anyInt())).thenReturn(flowOf(successResource))

        // Call the ViewModel method
        viewModel.getMovieDetail(movieId)

        val uiState = viewModel.uiState.first()
        TestCase.assertTrue(uiState is DetailMovieUiState.Success)
        TestCase.assertEquals((uiState as DetailMovieUiState.Success).movie, movie)
    }

    @Test
    fun `test getMovieDetail error`() = testCoroutineRule.runBlockingTest {
        // Define the error message
        val movieId = 1
        val errorMessage = "An error occurred"
        val errorResource = Resource.Error<Movie>(errorMessage)

        // Mock the behavior of repository.getDetailMovie()
        Mockito.`when`(movieRepository.getDetailMovie(anyInt())).thenReturn(flowOf(errorResource))

        // Call the ViewModel method
        viewModel.getMovieDetail(movieId)

        val uiState = viewModel.uiState.first()
        TestCase.assertTrue(uiState is DetailMovieUiState.Error)
        TestCase.assertEquals((uiState as DetailMovieUiState.Error).exception, errorMessage)
    }

    @Test
    fun `test setFavoriteMovie`() = testCoroutineRule.runBlockingTest {
        // Define a test movie and new favorite status
        val movie =  Movie(
            image = "",
            title = "test",
            overview = "testt"
        )
        val newFavoriteStatus = true // Set to true for testing

        // Mock the behavior of repository.setFavoriteMovie()
        Mockito.`when`(movieRepository.setFavoriteMovie(movie, newFavoriteStatus)).thenReturn(Unit)

        // Call the ViewModel method
        viewModel.setFavoriteGame(movie, newFavoriteStatus)

        // Verify that the repository's setFavoriteMovie was called with the correct parameters
        Mockito.verify(movieRepository).setFavoriteMovie(movie, newFavoriteStatus)

    }

    @Test
    fun `test getMovieReview success`() = testCoroutineRule.runBlockingTest {
        // Define the test data for movie reviews
        val movieId = 123
        val reviews = listOf(
            Review(
                id = "1",
                author = "ts",
                content ="lorem",
                createdAt = "",
                updatedAt = "",
                url = "http"
            )
        )
        val pagingData = PagingData.from(reviews)

        // Mock the behavior of repository.getMovieReviews()
        Mockito.`when`(movieRepository.getMovieReviews(movieId)).thenReturn(flowOf(pagingData))

        // Call the ViewModel method
        val result = viewModel.getMovieReview(movieId)

        // Verify that the result is not null
        assertNotNull(result)
    }

    @Test
    fun `test getMovieReview empty`() = testCoroutineRule.runBlockingTest {
        // Define the test data for an empty list of reviews
        val movieId = 123 // Replace with a valid movie ID
        val emptyReviews = emptyList<Review>()
        val pagingData = PagingData.from(emptyReviews)

        // Mock the behavior of repository.getMovieReviews()
        Mockito.`when`(movieRepository.getMovieReviews(movieId)).thenReturn(flowOf(pagingData))

        // Call the ViewModel method
        val result = viewModel.getMovieReview(movieId)

        // Verify that the result is not null
        assertNotNull(result)

    }


}