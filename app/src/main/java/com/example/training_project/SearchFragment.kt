package com.example.training_project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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

        binding.rvSearchMovies.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }

        binding.etSearch.addTextChangedListener { editable ->
            val query = editable?.toString()?.trim() ?: ""
            if (query.isNotEmpty()) {
                fetchSearchMovies(query)
            } else {
                searchAdapter.submitList(emptyList())
            }
        }
    }

    private fun fetchSearchMovies(query: String) {
        searchJob?.cancel()

        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            try {

                val response = withContext(Dispatchers.IO) {
                    RetrofitClients.instance.searchMovies(query = query)
                }

                if (response.results.isEmpty()) {
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