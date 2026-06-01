package com.example.training_project.ui.detail

import android.os.Bundle
import com.example.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.example.ui.R
import com.example.domain.model.Movie
import com.example.training_project.databinding.ActivityDetailBinding
import com.example.ui.observeNetwork
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }
    private lateinit var binding: ActivityDetailBinding
    override val viewModel: DetailViewModel by viewModel()
    private var movieId = -1L
    private var currentMovie: Movie? = null


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
            currentMovie?.let {
                viewModel.toggleFavorite(it)
            }
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
                currentMovie = it
                updateUI(it)
                viewModel.checkIsFavorite( it.id)
                viewModel.increaseDetailViewCount(it.id)
            }
        }
        viewModel.isFavorite.observe(this){resource ->
            handleApiState(resource){
                val icon = if (it) R.drawable.ic_save2 else R.drawable.ic_save
                binding.btnBookmark.setImageResource(icon)
            }
        }
        if (movieId != -1L) {
            viewModel.fetchMovieDetails(movieId)
        }
    }

    private fun updateUI(movie: Movie) {
        binding.tvMovieTitle.text = movie.title
        binding.tvRating.text = String.format("%.1f", movie.rating)
        binding.tvInfoYear.text = movie.releaseDate.take(4)

        val runtimeStr = "${movie.runtime} Minutes"
        binding.tvInfoDuration.text = runtimeStr

        binding.tvInfoGenre.text = movie.genres

        Glide.with(this)
            .load(movie.backdropUrl)
            .centerCrop()
            .into(binding.imgBanner)

        Glide.with(this)
            .load(movie.posterUrl)
            .centerCrop()
            .into(binding.imgPoster)
    }
}
