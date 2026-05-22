package com.example.training_project.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.training_project.ui.base.BaseFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training_project.ui.detail.DetailActivity
import com.example.training_project.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchMovieAdapter
    override val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        searchAdapter = SearchMovieAdapter { movie ->

            viewModel.saveSearchHistory(movie)

            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id)
            }
            startActivity(intent)
        }
        binding.rvSearchMovies.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
        if (viewModel.currentQuery.isEmpty()) {
            viewModel.getSearchHistory()
        }
    }

    override fun initListener() {
        binding.searchBar.onTextChanged { query ->
                viewModel.searchMovies(query)
        }
        binding.searchBar.onKeyboardSearchClick {}
        binding.root.apply {
            isClickable = true
            isFocusable = true
            setOnClickListener { binding.searchBar.hideKeyboard() }
        }
        binding.rvSearchMovies.setOnClickListener {
            binding.searchBar.hideKeyboard()
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observeLiveData() {
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                searchAdapter.submitList(movies)
            }
        }
        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.rvSearchMovies.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
        viewModel.searchHistory.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                if (viewModel.currentQuery.isEmpty()) {
                    searchAdapter.submitList(movies)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
