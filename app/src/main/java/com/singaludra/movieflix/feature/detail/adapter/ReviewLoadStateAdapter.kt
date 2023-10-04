package com.singaludra.movieflix.feature.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.singaludra.movieflix.R
import com.singaludra.movieflix.databinding.ItemReviewsLoadStateBinding

class ReviewLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ReviewLoadStateAdapter.MovieLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateViewHolder {
        return MovieLoadStateViewHolder.create(parent, retry)
    }

    class MovieLoadStateViewHolder(
        private val binding: ItemReviewsLoadStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnMoviesRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvMoviesErrorDescription.text = loadState.error.localizedMessage
            }
            binding.progressMoviesLoadMore.isVisible = loadState is LoadState.Loading
            binding.btnMoviesRetry.isVisible = loadState is LoadState.Error
            binding.tvMoviesErrorDescription.isVisible = loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_reviews_load_state, parent, false)
                val binding = ItemReviewsLoadStateBinding.bind(view)
                return MovieLoadStateViewHolder(binding, retry)
            }
        }
    }
}