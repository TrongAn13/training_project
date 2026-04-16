package com.example.training_project
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.training_project.databinding.ItemHomeMovieBinding
class HomeMovieAdapter(
    private val onItemClick: (HomeMovie) -> Unit
) : ListAdapter<HomeMovie, HomeMovieAdapter.HomeMovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieViewHolder {
        val binding = ItemHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class HomeMovieViewHolder(
        private val binding: ItemHomeMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(bindingAdapterPosition))
            }
        }
        fun bind(movie: HomeMovie) {
            binding.imgPoster.setImageResource(movie.posterResId)
        }
    }
    class MovieDiffCallback : DiffUtil.ItemCallback<HomeMovie>() {
        override fun areItemsTheSame(oldItem: HomeMovie, newItem: HomeMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeMovie, newItem: HomeMovie): Boolean {
            return oldItem == newItem
        }
    }
}
