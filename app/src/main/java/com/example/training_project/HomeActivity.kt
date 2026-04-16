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
            HomeMovie(1, posterResId = R.drawable.spiderman_poster),
            HomeMovie(2, posterResId = R.drawable.mv1),
            HomeMovie(3, posterResId = R.drawable.mv2),
            HomeMovie(4, posterResId = R.drawable.mv3),
            HomeMovie(5, posterResId = R.drawable.mv4),
            HomeMovie(6, posterResId = R.drawable.mv5),
            HomeMovie(7, posterResId = R.drawable.mv6),
            HomeMovie(8, posterResId = R.drawable.mv7)
        )

        binding.rvMovies.layoutManager = GridLayoutManager(this, 3)
        binding.rvMovies.setHasFixedSize(true)


        val movieAdapter = HomeMovieAdapter { clickedMovie ->
            val intent = Intent(this, DetailActivity::class.java)
            //gửi dữ liệu nhưng chưa hứng ở detail
            intent.putExtra("MOVIE_ID", clickedMovie.id)
            intent.putExtra("MOVIE_POSTER", clickedMovie.posterResId)

            startActivity(intent)
        }

        binding.rvMovies.adapter = movieAdapter

        movieAdapter.submitList(mockMovies)

        binding.btnMovieSpiderman.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}