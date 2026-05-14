package com.example.training_project.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.ui.base.LoadingType

class SearchViewModel(application: Application) : BaseViewModel(application){
    private val repository = MovieRepository()
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()

    fun searchMovies(query: String) {
        if (query.length < 3) {
            searchResults.value =
                Resource.Success(emptyList())
            isEmpty.value = false
            return
        }

        searchResults.value = Resource.Loading
        executeApi(searchResults, LoadingType.NORMAL) {
            val response = repository.searchMoviesFromApi(query)
            val results = response.results ?: emptyList()
            isEmpty.postValue( results.isEmpty())
            results
        }
    }
}
