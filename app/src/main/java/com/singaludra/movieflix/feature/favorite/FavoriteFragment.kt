package com.singaludra.movieflix.feature.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.singaludra.domain.model.Movie
import com.singaludra.movieflix.databinding.FragmentFavoriteBinding
import com.singaludra.movieflix.feature.common.MovieAdapter
import com.singaludra.movieflix.feature.detail.DetailMovieActivity
import com.singaludra.movieflix.feature.movies.MovieUiState
import com.singaludra.movieflix.feature.movies.MovieViewModel
import com.singaludra.movieflix.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var movieAdapter: MovieAdapter

    private val viewModel by viewModels<FavoriteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewObserve()
    }

    private fun onViewObserve() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is FavriteMovieUiState.Success -> {
                            showMovies(uiState.movie)
                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is FavriteMovieUiState.Error -> {
                            showError(uiState.exception)
                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is FavriteMovieUiState.Loading -> {
                            binding.progressBarMovies.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showError(exception: String) {
        requireContext().shortToast(exception)
    }

    private fun showMovies(movie: List<Movie>) {
        movieAdapter = MovieAdapter(object : MovieAdapter.OnClickListener{
            override fun onClickItem(item: Movie) {
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, item.id)
                intent.putExtra(DetailMovieActivity.IS_FAVORITE, item.isFavorite)
                startActivity(intent)
            }

        })
        movieAdapter.differ.submitList(movie)
        binding.rvMovies.adapter = movieAdapter
    }

}