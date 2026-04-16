package com.example.training_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.training_project.databinding.ItemHomeMovieBinding
class HomeMovieAdapter(
    private val movies: List<HomeMovie>,
    private val onItemClick: (HomeMovie) -> Unit
) : RecyclerView.Adapter<HomeMovieAdapter.HomeMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieViewHolder {
        val binding = ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class HomeMovieViewHolder(
        private val binding: ItemHomeMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: HomeMovie) {
            binding.imgPoster.setImageResource(movie.posterResId)
            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }
}
