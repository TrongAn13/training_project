package com.example.training_project.ui.favorite

import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.ui.base.LoadingType
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider

class FavoriteViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    val favoriteMovies = MutableLiveData<Resource<List<Movie>>>()
    fun getFavoriteMovies() {
        executeApi(favoriteMovies,LoadingType.NONE) {
            useCases.getFavoriteMovies()
        }
    }
}