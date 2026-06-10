package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.repository.MovieRepository

class RefreshMoviesUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(category: MovieCategory, page: Int = 1): List<Movie> {
        return repository.refreshMovies(category, page)
    }
}
