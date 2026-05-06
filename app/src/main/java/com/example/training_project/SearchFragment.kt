package com.example.training_project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training_project.databinding.FragmentSearchBinding
import com.example.training_project.network.RetrofitClients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Job

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchJob: Job? = null

    private lateinit var searchAdapter: SearchMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchMovieAdapter { movie ->
            val intent = android.content.Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id ?: -1L)
            }
            startActivity(intent)
        }

        binding.searchBar.focusAndShowKeyboard()

        binding.rvSearchMovies.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }

        binding.searchBar.onTextChanged { query ->
            if (query.length >= 3) {
                fetchSearchMovies(query)
            } else if (query.isEmpty()) {
                searchAdapter.submitList(emptyList())
            }
        }
        binding.searchBar.onKeyboardSearchClick {
            hideKeyboard()
        }
        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.rvSearchMovies.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(binding.searchBar.getSearchWindowToken(), 0)
        binding.searchBar.clearSearchFocus()
    }

    private fun fetchSearchMovies(query: String) {
        searchJob?.cancel()

        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            try {

                val response = withContext(Dispatchers.IO) {
                    RetrofitClients.instance.searchMovies(query = query)
                }

                if (response.results?.isEmpty()?:true) {
                    binding.rvSearchMovies.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                    searchAdapter.submitList(emptyList())
                } else {
                    binding.rvSearchMovies.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility = View.GONE
                    searchAdapter.submitList(response.results)
                }

            } catch (e: Exception) {
                Log.e("SearchFragment", "Lỗi khi tìm kiếm: ${e.message}")

                searchAdapter.submitList(emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}