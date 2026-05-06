package com.example.training_project
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.training_project.databinding.ActivityDetailBinding
import com.example.training_project.network.Movie
import com.example.training_project.network.RetrofitClients
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }

    private lateinit var binding: ActivityDetailBinding
    var movie: Movie? = null
    val movieLiveData = MutableLiveData<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieLiveData.observe(this) { movieData ->
            this.movie = movieData
            updateUI(movieData)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnBookmark.setOnClickListener {
            Toast.makeText(this, R.string.save_movie, Toast.LENGTH_SHORT).show()
        }

        val movieId = intent.getLongExtra(EXTRA_MOVIE_ID, -1L)
        if (movieId != -1L) {
            fetchMovieDetails(movieId)
        }

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

    private fun fetchMovieDetails(movieId: Long) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClients.instance.getMovieDetails(movieId = movieId)
                }
                movieLiveData.postValue(response)

            } catch (e: Exception) {
                Log.e("DetailActivity", "Error fetching movie details: ${e.message}")
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