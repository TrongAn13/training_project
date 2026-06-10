package com.example.training_project.ui.favorite

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCases
import com.example.ui.base.BaseViewModel
import com.example.ui.base.LoadingType
import com.example.ui.Resource
import com.example.ui.ResourceProvider

class FavoriteViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    val favoriteMovies = MutableLiveData<Resource<List<Movie>>>()
    fun getFavoriteMovies() {
        executeApi(favoriteMovies,LoadingType.NONE) {
            useCases.getFavoriteMovies()
        }
    }
}