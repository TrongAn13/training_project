package com.example.training_project.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.training_project.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.example.training_project.R
import com.example.training_project.data.model.Movie
import com.example.training_project.databinding.ActivityDetailBinding
import com.example.training_project.utils.observeNetwork
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : BaseActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }
    private lateinit var binding: ActivityDetailBinding
    override val viewModel: DetailViewModel by viewModels()
    private var movieId = -1L


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieId = intent.getLongExtra(EXTRA_MOVIE_ID, -1L)
        super.onCreate(savedInstanceState)
    }
    override fun initListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnBookmark.setOnClickListener {
            Toast.makeText(this, R.string.save_movie, Toast.LENGTH_SHORT).show()
        }
        observeNetwork(binding.root) {
            viewModel.retry()
        }
    }

    override fun initView() {
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
    override fun observeLiveData() {
        viewModel.movie.observe(this) { resource ->
            handleApiState(resource) {
                updateUI(it)
            }
        }
        if (movieId != -1L) {
            viewModel.fetchMovieDetails(movieId)
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
