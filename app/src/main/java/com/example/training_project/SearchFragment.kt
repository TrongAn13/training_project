package com.example.training_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
            if (query.length >=3) {
                fetchSearchMovies(query)
            } else if(query.isEmpty()) {
                searchAdapter.submitList(emptyList())
            }
        }
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                true
            } else {
                false
            }
        }
        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.rvSearchMovies.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
    }
    private fun hideKeyboard() {
        binding.etSearch.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
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