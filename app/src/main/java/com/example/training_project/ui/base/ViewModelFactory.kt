package com.example.training_project.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.home.HomeViewModel
import com.example.training_project.ui.detail.DetailViewModel
import com.example.training_project.ui.search.SearchViewModel

class ViewModelFactory(private val application: Application, private val useCases: MovieUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(application,useCases) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(application,useCases) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(application,useCases) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
