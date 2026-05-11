package com.example.training_project

import android.app.Application
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.usecase.GetMovieDetailsUseCase
import com.example.training_project.domain.usecase.GetMoviesUseCase
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.domain.usecase.SearchMoviesUseCase

class MovieApplication : Application() {
    lateinit var repository: MovieRepositoryImpl
    lateinit var movieUseCases: MovieUseCases

    override fun onCreate() {
        super.onCreate()

        repository = MovieRepositoryImpl.getInstance(this)

        movieUseCases = MovieUseCases(

            getMovies = GetMoviesUseCase(repository),

            searchMovies = SearchMoviesUseCase(repository),

            getMovieDetails = GetMovieDetailsUseCase(repository)
        )
    }
}
