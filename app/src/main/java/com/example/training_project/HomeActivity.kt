package com.example.training_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mockMovies = listOf(
            HomeMovie(posterResId = R.drawable.spiderman_poster),
            HomeMovie(posterResId = R.drawable.mv1),
            HomeMovie(posterResId = R.drawable.mv2),
            HomeMovie(posterResId = R.drawable.mv3),
            HomeMovie(posterResId = R.drawable.mv4),
            HomeMovie(posterResId = R.drawable.mv5),
            HomeMovie(posterResId = R.drawable.mv6),
            HomeMovie(posterResId = R.drawable.mv7)
        )

        binding.rvMovies.layoutManager = GridLayoutManager(this, 3)
        binding.rvMovies.setHasFixedSize(true)
        binding.rvMovies.adapter = HomeMovieAdapter(mockMovies) {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        binding.btnMovieSpiderman.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}