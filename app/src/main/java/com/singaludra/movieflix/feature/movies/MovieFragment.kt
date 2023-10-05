package com.singaludra.movieflix.feature.movies

import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room.util.query
import com.singaludra.domain.model.Movie
import com.singaludra.movieflix.databinding.FragmentMovieBinding
import com.singaludra.movieflix.feature.common.MovieAdapter
import com.singaludra.movieflix.feature.detail.DetailMovieActivity
import com.singaludra.movieflix.utils.getQueryTextChangeStateFlow
import com.singaludra.movieflix.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    private val viewModel by viewModels<MovieViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewObserve()
        onViewBind()
    }

    private fun onViewBind() {
        with(binding){
            svMovie.setOnCloseListener {
                viewModel.getMovies()
                true
            }
            svMovie.queryHint = "Cari movie"
            svMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (!p0.isNullOrEmpty()){
                        viewModel.searchMovies(p0)
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if(p0.isNullOrBlank()){
                        viewModel.getMovies()
                    }
                    return true
                }

            })
        }
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
                        is MovieUiState.Success -> {
                            showMovies(uiState.movie)
                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is MovieUiState.Error -> {
                            showError(uiState.exception)
                            binding.progressBarMovies.visibility = View.GONE
                        }
                        is MovieUiState.Loading -> {
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