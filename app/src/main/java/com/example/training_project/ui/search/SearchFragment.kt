package com.example.training_project.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.training_project.ui.base.BaseFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training_project.ui.detail.DetailActivity
import com.example.training_project.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchMovieAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }
    private fun setupRecyclerView(){
        searchAdapter = SearchMovieAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.Companion.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }
        binding.rvSearchMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }
    private fun setupListeners(){
        binding.searchBar.focusAndShowKeyboard()
        binding.searchBar.onTextChanged { query ->
        viewModel.searchMovies(query)
        }
        binding.searchBar.onKeyboardSearchClick {}
        binding.root.apply {
            isClickable = true
            isFocusable = true
            setOnClickListener {
                binding.searchBar.hideKeyboard()
            }
        }

        binding.rvSearchMovies.setOnClickListener {
            binding.searchBar.hideKeyboard()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun observeViewModel(){
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movies ->
                searchAdapter.submitList(movies)
            }
        }
        viewModel.isEmpty.observe(viewLifecycleOwner){ isEmpty ->
            if (isEmpty) {
                binding.rvSearchMovies.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.rvSearchMovies.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
