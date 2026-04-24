package com.example.training_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 3)

        val movieAdapter = HomeMovieAdapter { clickedMovie ->
            openDetail(clickedMovie.id)
        }

        binding.rvMovies.adapter = movieAdapter

        movieAdapter.submitList(mockMovies)

        binding.swipeRefreshHome.setColorSchemeResources(R.color.primary_blue)
        binding.swipeRefreshHome.setOnRefreshListener {
            movieAdapter.submitList(mockMovies.shuffled())
            binding.swipeRefreshHome.isRefreshing = false
        }

        binding.cardMovie1.setOnClickListener { openDetail(2) }
        binding.btnMovieSpiderman.setOnClickListener { openDetail(1) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun openDetail(movieId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId)
        startActivity(intent)
    }


}