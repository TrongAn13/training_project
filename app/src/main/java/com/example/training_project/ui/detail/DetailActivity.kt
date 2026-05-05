package com.example.training_project.ui.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.training_project.R
import com.example.training_project.data.model.Movie
import com.example.training_project.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupViewPager()
        observeViewModel()
        val movieId = intent.getLongExtra(EXTRA_MOVIE_ID, -1L)
        if (viewModel.movie.value == null) {
            viewModel.fetchMovieDetails(movieId)
        }
    }
    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnBookmark.setOnClickListener {
            Toast.makeText(this, R.string.save_movie, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewPager() {
        val pagerAdapter = DetailPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.about_movie)
                1 -> getString(R.string.reviews)
                2 -> getString(R.string.cast)
                else -> ""
            }
        }.attach()
    }
    private fun observeViewModel() {
        viewModel.movie.observe(this) { movieData ->
            updateUI(movieData)
        }
        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Log.e("DetailActivity", "Lỗi mạng: $error")
                Toast.makeText(this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(movie: Movie) {
        binding.tvMovieTitle.text = movie.title ?: ""
        binding.tvRating.text = String.format("%.1f", movie.voteAverage ?: 0.0)
        binding.tvInfoYear.text = movie.releaseDate?.take(4) ?: ""

        val runtimeStr = movie.runtime?.let { "$it Minutes" } ?: ""
        binding.tvInfoDuration.text = runtimeStr

        binding.tvInfoGenre.text = movie.getGenresText()

        Glide.with(this)
            .load(movie.getBackdropUrl())
            .centerCrop()
            .into(binding.imgBanner)

        Glide.with(this)
            .load(movie.getPosterUrl())
            .centerCrop()
            .into(binding.imgPoster)
    }
}