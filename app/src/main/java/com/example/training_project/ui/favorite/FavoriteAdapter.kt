package com.example.training_project.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.training_project.databinding.ItemSearchMovieBinding
import com.example.domain.model.Movie

class FavoriteAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemSearchMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(
        private val binding: ItemSearchMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                tvTitle.text = movie.title
                tvRating.text = String.format("%.1f", movie.rating)
                tvGenre.text = movie.genres
                tvYear.text = if (movie.releaseDate.length >= 4) movie.releaseDate.take(4) else ""
                tvDuration.text = "${movie.runtime} Minutes"

                Glide.with(ivPoster)
                    .load(movie.posterUrl)
                    .transform(CenterCrop(), RoundedCorners(40))
                    .into(ivPoster)
            }
        }
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}
