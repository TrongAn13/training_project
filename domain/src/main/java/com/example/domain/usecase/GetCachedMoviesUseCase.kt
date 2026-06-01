package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.repository.MovieRepository

class GetCachedMoviesUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(category: MovieCategory): List<Movie> {
        return repository.getCachedMovies(category)
    }
}