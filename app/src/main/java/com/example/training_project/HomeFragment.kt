package com.example.training_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.FragmentHomeBinding
import com.example.training_project.network.RetrofitClients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class HomeFragment : Fragment() {

    enum class MovieTab {
        NOW_PLAYING, UPCOMING, TOP_RATED, POPULAR
    }

    private var currentTab = MovieTab.NOW_PLAYING
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: HomeMovieAdapter
    private lateinit var trendingAdapter: TrendingMovieAdapter
    private var currentPage = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = HomeMovieAdapter { movie ->
            val intent = android.content.Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = movieAdapter
        }

        trendingAdapter = TrendingMovieAdapter { movie ->
            val intent = android.content.Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }
        binding.layoutTopHeader.rvTrendingMovies.adapter = trendingAdapter

        binding.rvMovies.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            currentPage++
                            fetchMovies()
                        }
                    }
                }
            }
        })

        binding.tabNowPlaying.setOnClickListener { switchTab(MovieTab.NOW_PLAYING) }
        binding.tabUpcoming.setOnClickListener { switchTab(MovieTab.UPCOMING) }
        binding.tabTopRated.setOnClickListener { switchTab(MovieTab.TOP_RATED) }
        binding.tabPopular.setOnClickListener { switchTab(MovieTab.POPULAR) }

        binding.swipeRefreshHome.setOnRefreshListener {
            currentPage = 1
            fetchMovies()
            fetchTrendingMovies()
        }

        updateTabColors()
        fetchMovies()
        fetchTrendingMovies()
    }

    private fun switchTab(tab: MovieTab) {
        if (currentTab == tab) return
        currentTab = tab

        updateTabColors()
        binding.rvMovies.scrollToPosition(0)
        currentPage = 1
        fetchMovies()
    }

    private fun fetchTrendingMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClients.instance.getTrendingMovies()
                }
                trendingAdapter.submitList(response.results)
            } catch (e: Exception) {
                Log.e("API", "Lỗi tải Trending: ${e.message}")
            }
        }
    }
    private fun fetchMovies() {
        if (currentPage == 1) {
            binding.swipeRefreshHome.isRefreshing = true
        }
        isLoading = true
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    when (currentTab) {
                        MovieTab.NOW_PLAYING -> RetrofitClients.instance.getNowPlayingMovies(page = currentPage)
                        MovieTab.UPCOMING -> RetrofitClients.instance.getUpcomingMovies(page = currentPage)
                        MovieTab.TOP_RATED -> RetrofitClients.instance.getTopRatedMovies(page = currentPage)
                        MovieTab.POPULAR -> RetrofitClients.instance.getPopularMovies(page = currentPage)
                    }
                }
                
                if (currentPage == 1) {
                    movieAdapter.submitList(response.results)
                } else {
                    val currentList = movieAdapter.currentList.toMutableList()
                    currentList.addAll(response.results)
                    movieAdapter.submitList(currentList)
                }

            } catch (e: Exception) {
                Log.e("API", "Lỗi: ${e.message}")
            } finally {
                binding.swipeRefreshHome.isRefreshing = false
                isLoading = false
            }
        }
    }
    private fun updateTabColors() {
        val unselectedColor = ContextCompat.getColor(requireContext(), R.color.search_background)
        val selectedColor = ContextCompat.getColor(requireContext(),R.color.white)

        binding.tvNowPlaying.setTextColor(unselectedColor)
        binding.indicatorNowPlaying.visibility = View.GONE

        binding.tvUpcoming.setTextColor(unselectedColor)
        binding.indicatorUpcoming.visibility = View.GONE

        binding.tvTopRated.setTextColor(unselectedColor)
        binding.indicatorTopRated.visibility = View.GONE

        binding.tvPopular.setTextColor(unselectedColor)
        binding.indicatorPopular.visibility = View.GONE

        when (currentTab) {
            MovieTab.NOW_PLAYING -> {
                binding.tvNowPlaying.setTextColor(selectedColor)
                binding.indicatorNowPlaying.visibility = View.VISIBLE
            }
            MovieTab.UPCOMING -> {
                binding.tvUpcoming.setTextColor(selectedColor)
                binding.indicatorUpcoming.visibility = View.VISIBLE
            }
            MovieTab.TOP_RATED -> {
                binding.tvTopRated.setTextColor(selectedColor)
                binding.indicatorTopRated.visibility = View.VISIBLE
            }
            MovieTab.POPULAR -> {
                binding.tvPopular.setTextColor(selectedColor)
                binding.indicatorPopular.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
