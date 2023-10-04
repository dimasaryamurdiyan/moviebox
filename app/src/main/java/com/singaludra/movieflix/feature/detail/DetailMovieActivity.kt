package com.singaludra.movieflix.feature.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.singaludra.domain.model.Movie
import com.singaludra.movieflix.BuildConfig
import com.singaludra.movieflix.R
import com.singaludra.movieflix.databinding.ActivityDetailMovieBinding
import com.singaludra.movieflix.databinding.ViewGenreChipBinding
import com.singaludra.movieflix.feature.detail.adapter.ReviewAdapter
import com.singaludra.movieflix.feature.detail.adapter.ReviewLoadStateAdapter
import com.singaludra.movieflix.utils.hourMinutes
import com.singaludra.movieflix.utils.loadImage
import com.singaludra.movieflix.utils.roundTo
import com.singaludra.movieflix.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding

    private val viewModel by viewModels<DetailMovieViewModel>()

    private val movieId by lazy {
        intent.getIntExtra(EXTRA_DATA, 0)
    }

    private val reviewAdapter by lazy {
        ReviewAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onViewBind()
        fetchViewModelCallback()
        onViewObserve()
    }

    private fun onViewBind() {
        binding.apply {
            rvReview.apply {
                layoutManager = LinearLayoutManager(this@DetailMovieActivity, LinearLayoutManager.HORIZONTAL ,false)
                adapter = reviewAdapter.withLoadStateHeaderAndFooter(
                    header = ReviewLoadStateAdapter{reviewAdapter.retry()},
                    footer = ReviewLoadStateAdapter{reviewAdapter.retry()}
                )

                reviewAdapter.addLoadStateListener { loadState -> setupReviewMovie(loadState) }
            }
            icBackButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun setupReviewMovie(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && reviewAdapter.itemCount == 0

        binding.rvReview.isVisible = !isListEmpty
        binding.tvEmpty.isVisible = isListEmpty

        // Only shows the list if refresh succeeds.
        binding.rvReview.isVisible = loadState.source.refresh is LoadState.NotLoading
        // Show the retry state if initial load or refresh fails.
        binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
    }

    private fun fetchViewModelCallback() {
        viewModel.getMovieDetail(movieId)
    }

    private fun onViewObserve() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED

                //region get movie review
                viewModel.getMovieReview(movieId).collectLatest {
                    reviewAdapter.submitData(it)
                }
                //end region

                //region detail movie ui state
                viewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is DetailMovieUiState.Success -> {
                            showDetailMovie(uiState.movie)
//                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is DetailMovieUiState.Error -> {
                            showError(uiState.exception)
//                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is DetailMovieUiState.Loading -> {
//                            binding.progressBarMovies.visibility = View.VISIBLE
                        }
                    }
                }

            }
        }
    }

    private fun showError(exception: String) {
        this@DetailMovieActivity.shortToast(exception)
    }

    private fun showDetailMovie(movie: Movie) {
        with(binding){
            tvTitle.text = movie.title
            tvOverview.text= movie.overview
            ivImgCourse.loadImage(
                BuildConfig.BACKDROP_PATH + movie.backdropImage
            )

            tvLanguage.text = movie.originalLanguage
            tvDuration.text = movie.runtime?.hourMinutes()
            tvRating.text = movie.voteAverage?.roundTo(1).toString()
            tvReleaseDate.text = movie.releaseDate

            tvMovieTitle.text = movie.title

            //region populate genre
            val genreList = movie.genres?.map {it.name}
            genreList?.forEach {
                val chip = createChip(it)
                chipGroup.addView(chip)
            }
            // end region populate genre

            //region favorite state
            var statusFavorite = movie.isFavorite
            setStatusFavorite(statusFavorite)
            fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                viewModel.setFavoriteGame(movie, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
            //end region
        }
    }

    private fun createChip(label: String): Chip {
        val chip = ViewGenreChipBinding.inflate(layoutInflater).root
        chip.text = label
        chip.isClickable = false
        return chip
    }

    private fun setStatusFavorite(statusFavorite: Boolean?) {
        if (statusFavorite == true) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_filled_24))
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_outline_24))
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}