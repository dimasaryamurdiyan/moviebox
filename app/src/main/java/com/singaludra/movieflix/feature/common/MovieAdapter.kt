package com.singaludra.movieflix.feature.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.singaludra.domain.model.Movie
import com.singaludra.movieflix.BuildConfig
import com.singaludra.movieflix.databinding.ItemMoviePosterBinding
import com.singaludra.movieflix.utils.loadImage

class MovieAdapter(private val itemCLick: OnClickListener): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie){
            binding.apply {
                ivPoster.loadImage(BuildConfig.POSTER_PATH+item.image)
                tvTitle.text = item.title

                binding.root.setOnClickListener {
                    itemCLick.onClickItem(item)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemMoviePosterBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)


    interface OnClickListener {
        fun onClickItem(item: Movie)
    }
}