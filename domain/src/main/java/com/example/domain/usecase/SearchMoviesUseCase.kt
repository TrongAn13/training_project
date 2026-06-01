package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository

class SearchMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(query: String): List<Movie> {
        if (query.isBlank()) return emptyList()
        return repository.searchMovies(query)
    }
}