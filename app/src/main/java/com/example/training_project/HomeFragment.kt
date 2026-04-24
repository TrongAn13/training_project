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
    private var currentTab = "NOW_PLAYING"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: HomeMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = HomeMovieAdapter { movie ->  }
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = movieAdapter
        }

        binding.tabNowPlaying.setOnClickListener { switchTab("NOW_PLAYING") }
        binding.tabUpcoming.setOnClickListener { switchTab("UPCOMING") }
        binding.tabTopRated.setOnClickListener { switchTab("TOP_RATED") }
        binding.tabPopular.setOnClickListener { switchTab("POPULAR") }

        binding.swipeRefreshHome.setOnRefreshListener {
            fetchMovies()
        }

        updateTabColors()
        fetchMovies()
    }

    private fun switchTab(tabName: String) {
        if (currentTab == tabName) return
        currentTab = tabName

        updateTabColors()
        binding.rvMovies.scrollToPosition(0)
        fetchMovies()
    }
    private fun fetchMovies() {
        binding.swipeRefreshHome.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    when (currentTab) {
                        "NOW_PLAYING" -> RetrofitClients.instance.getNowPlayingMovies()
                        "UPCOMING" -> RetrofitClients.instance.getUpcomingMovies()
                        "TOP_RATED" -> RetrofitClients.instance.getTopRatedMovies()
                        "POPULAR" -> RetrofitClients.instance.getPopularMovies()
                        else -> RetrofitClients.instance.getPopularMovies()
                    }
                }
                movieAdapter.submitList(response.results)

            } catch (e: Exception) {
                Log.e("API", "Lỗi: ${e.message}")
            } finally {
                binding.swipeRefreshHome.isRefreshing = false
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
            "NOW_PLAYING" -> {
                binding.tvNowPlaying.setTextColor(selectedColor)
                binding.indicatorNowPlaying.visibility = View.VISIBLE
            }
            "UPCOMING" -> {
                binding.tvUpcoming.setTextColor(selectedColor)
                binding.indicatorUpcoming.visibility = View.VISIBLE
            }
            "TOP_RATED" -> {
                binding.tvTopRated.setTextColor(selectedColor)
                binding.indicatorTopRated.visibility = View.VISIBLE
            }
            "POPULAR" -> {
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
