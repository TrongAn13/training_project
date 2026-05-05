package com.example.training_project.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training_project.R
import com.example.training_project.data.model.Movie
import com.example.training_project.databinding.ItemHomeMovieBinding

class HomeMovieAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, HomeMovieAdapter.HomeMovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieViewHolder {
        val binding = ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieViewHolder(binding) { position ->
            onItemClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: HomeMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class HomeMovieViewHolder(
        private val binding: ItemHomeMovieBinding,
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

        fun bind(movie: Movie) {
            val posterUrl = movie.getPosterUrl()

            Glide.with(binding.imgPoster)
                .load(posterUrl.ifEmpty { R.drawable.ic_launcher_foreground })
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(binding.imgPoster)
        }
    }
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }
}