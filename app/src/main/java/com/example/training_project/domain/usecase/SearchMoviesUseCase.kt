package com.example.training_project.domain.usecase

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository

class SearchMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(query: String): List<Movie> {
        if (query.isBlank()) return emptyList()
        return repository.searchMovies(query)
    }
}