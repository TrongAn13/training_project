package com.example.training_project.di

import com.example.training_project.data.local.AppDatabase
import com.example.training_project.data.network.RetrofitClients
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.repository.MovieRepository
import com.example.training_project.domain.usecase.GetCachedMoviesUseCase
import com.example.training_project.domain.usecase.GetMovieDetailsUseCase
import com.example.training_project.domain.usecase.GetMoviesUseCase
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.domain.usecase.RefreshMoviesUseCase
import com.example.training_project.domain.usecase.SearchMoviesUseCase
import com.example.training_project.ui.detail.DetailViewModel
import com.example.training_project.ui.home.HomeViewModel
import com.example.training_project.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        RetrofitClients.instance
    }
    single {
        AppDatabase.getDatabase(androidApplication())
    }
    single {
        get<AppDatabase>().movieDao()
    }
    single<MovieRepository> {
        MovieRepositoryImpl(apiService = get(), movieDao = get())
    }
    single {
        MovieUseCases(
            getMovies = GetMoviesUseCase(repository = get()),
            searchMovies = SearchMoviesUseCase(repository = get()),
            getMovieDetails = GetMovieDetailsUseCase(repository = get()),
            getCachedMovies = GetCachedMoviesUseCase(repository = get()),
            refreshMovies = RefreshMoviesUseCase(repository = get())
        )
    }
    viewModel {
        HomeViewModel(application = androidApplication(), useCases = get()
        )
    }

    viewModel {
        DetailViewModel(application = androidApplication(), useCases = get()
        )
    }

    viewModel {
        SearchViewModel(application = androidApplication(), useCases = get()
        )
    }
}