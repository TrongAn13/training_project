package com.example.training_project.domain.usecase

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.MovieCategory
import com.example.training_project.domain.repository.MovieRepository

class GetCachedMoviesUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(category: MovieCategory): List<Movie> {
        return repository.getCachedMovies(category)
    }
}