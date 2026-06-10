package com.example.training_project.domain.usecase

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository

class GetSearchHistoryUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(): List<Movie> {
        return repository.getSearchHistory()
    }
}
