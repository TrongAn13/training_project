package com.example.training_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.training_project.databinding.ItemCastBinding
import com.example.training_project.network.Cast

class CastAdapter : ListAdapter<Cast, CastAdapter.CastViewHolder>(CastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CastViewHolder(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.tvCastName.text = cast.name

            Glide.with(binding.imgCast)
                .load(cast.getProfileUrl().ifEmpty { R.drawable.rv })
                .centerCrop()
                .placeholder(R.drawable.rv)
                .into(binding.imgCast)
        }
    }

    class CastDiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
}
