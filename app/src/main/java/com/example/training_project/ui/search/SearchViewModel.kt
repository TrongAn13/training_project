package com.example.training_project.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.ui.base.LoadingType

class SearchViewModel(application: Application,private val useCases: MovieUseCases) : BaseViewModel(application) {
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
