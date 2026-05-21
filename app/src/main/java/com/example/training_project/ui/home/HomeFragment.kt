package com.example.training_project.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.training_project.ui.base.BaseFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.training_project.ui.detail.DetailActivity
import com.example.training_project.R
import com.example.training_project.databinding.FragmentHomeBinding
import com.example.training_project.domain.model.MovieTab
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override val viewModel: HomeViewModel by viewModel()
    private lateinit var movieAdapter: HomeMovieAdapter
    private lateinit var trendingAdapter: TrendingMovieAdapter
    private val threshold = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        movieAdapter = HomeMovieAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }

        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = movieAdapter
        }

        trendingAdapter = TrendingMovieAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }
        binding.layoutTopHeader.rvTrendingMovies.adapter = trendingAdapter
        updateTabColors()
    }

    override fun initListener() {
        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isPaginating && viewModel.canLoadMore && (visibleItemCount + pastVisibleItems) >= totalItemCount - threshold) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
        binding.tabNowPlaying.setOnClickListener { switchTabUI(MovieTab.NOW_PLAYING) }
        binding.tabUpcoming.setOnClickListener { switchTabUI(MovieTab.UPCOMING) }
        binding.tabTopRated.setOnClickListener { switchTabUI(MovieTab.TOP_RATED) }
        binding.tabPopular.setOnClickListener { switchTabUI(MovieTab.POPULAR) }

        binding.swipeRefreshHome.setOnRefreshListener {
            viewModel.refreshData()
        }

        binding.layoutTopHeader.searchBar.setReadOnlyMode {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNav.selectedItemId = R.id.searchFragment
        }
    }

    private fun switchTabUI(tab: MovieTab) {
        if (viewModel.currentTab == tab) return
        viewModel.switchTab(tab)
        binding.tabNowPlaying.isSelected = tab == MovieTab.NOW_PLAYING
        binding.tabUpcoming.isSelected = tab == MovieTab.UPCOMING
        binding.tabTopRated.isSelected = tab == MovieTab.TOP_RATED
        binding.tabPopular.isSelected = tab == MovieTab.POPULAR
        updateTabColors()
        binding.rvMovies.scrollToPosition(0)
    }

    override fun observeLiveData() {
        viewModel.trendingMovies.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                trendingAdapter.submitList(movies)
            }
        }

        viewModel.tabMovies.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                movieAdapter.submitList(movies)
            }
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshHome.isRefreshing = isRefreshing
        }
    }

    private fun updateTabColors() {
        val unselectedColor = ContextCompat.getColor(requireContext(), R.color.search_background)
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.tvNowPlaying.setTextColor(unselectedColor)
        binding.indicatorNowPlaying.visibility = View.GONE
        binding.tvUpcoming.setTextColor(unselectedColor)
        binding.indicatorUpcoming.visibility = View.GONE
        binding.tvTopRated.setTextColor(unselectedColor)
        binding.indicatorTopRated.visibility = View.GONE
        binding.tvPopular.setTextColor(unselectedColor)
        binding.indicatorPopular.visibility = View.GONE

        when (viewModel.currentTab) {
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
