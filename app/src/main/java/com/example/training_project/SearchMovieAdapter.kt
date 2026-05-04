package com.example.training_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.training_project.databinding.ItemSearchMovieBinding
import com.example.training_project.network.Movie

class SearchMovieAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, SearchMovieAdapter.SearchViewHolder>(HomeMovieAdapter.MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding) { position ->
            onItemClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        private val binding: ItemSearchMovieBinding,
        private val onClickAtPosition: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) onClickAtPosition(position)
            }
        }

        fun bind(movie: Movie) {
            binding.tvTitle.text = movie.title
            binding.tvRating.text = movie.voteAverage?.toString()
            binding.tvGenre.text = movie.getGenresText()
            binding.tvYear.text = movie.releaseDate?.take(4)
//            binding.tvDuration.text = movie.
            Glide.with(binding.root.context)
                .load(movie.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.ivPoster)
        }
    }
}