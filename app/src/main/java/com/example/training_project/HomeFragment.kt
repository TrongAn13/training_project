package com.example.training_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private fun openDetail(movieId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId)
        startActivity(intent)
    }

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
        binding.rvMovies.setHasFixedSize(true)


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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}