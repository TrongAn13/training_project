package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource

class SearchViewModel(private val useCases: MovieUseCases) : BaseViewModel() {
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()

    fun searchMovies(query: String) {
        if (query.length < 3) {
            searchResults.value = Resource.Success(emptyList())
            isEmpty.value = false
            return
        }

        executeApi(searchResults) {
            val results = useCases.searchMovies(query)
            isEmpty.postValue(results.isEmpty())
            results
        }
    }
}
