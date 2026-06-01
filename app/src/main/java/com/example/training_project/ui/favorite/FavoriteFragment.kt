package com.example.training_project.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training_project.databinding.FragmentFavoriteBinding
import com.example.ui.base.BaseFragment
import com.example.training_project.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class FavoriteFragment : BaseFragment() {
    private var _binding: FragmentFavoriteBinding? = null

    override val viewModel: FavoriteViewModel by activityViewModel()
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        favoriteAdapter = FavoriteAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id)
            }
            startActivity(intent)
        }
        binding.rvSearchMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
        binding.rvSearchMovies.adapter = favoriteAdapter
        viewModel.getFavoriteMovies()
    }
    override fun initListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteMovies()
    }

    override fun observeLiveData() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                if (movies.isEmpty()) {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.rvSearchMovies.visibility = View.GONE
                } else {
                    favoriteAdapter.submitList(movies)
                    binding.layoutEmpty.visibility = View.GONE
                    binding.rvSearchMovies.visibility = View.VISIBLE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}