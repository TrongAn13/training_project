package com.example.training_project.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ui.R
import com.example.domain.model.Movie
import com.example.training_project.databinding.ItemTrendingMovieBinding

class TrendingMovieAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, TrendingMovieAdapter.TrendingViewHolder>(HomeMovieAdapter.MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val binding = ItemTrendingMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingViewHolder(binding) { position ->
            onItemClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class TrendingViewHolder(
        private val binding: ItemTrendingMovieBinding,
        private val onClickAtPosition: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClickAtPosition(position)
                }
            }
        }

        fun bind(movie: Movie, position: Int) {
            binding.tvTrendingRank.text = (position + 1).toString()

            Glide.with(binding.ivTrendingPoster)
                .load(movie.posterUrl.ifEmpty { R.drawable.ic_launcher_foreground })
                .centerCrop()
                .into(binding.ivTrendingPoster)
        }
    }
}