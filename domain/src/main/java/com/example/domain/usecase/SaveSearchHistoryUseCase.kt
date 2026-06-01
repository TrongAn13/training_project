package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository

class SaveSearchHistoryUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: Movie) {
        repository.saveSearchHistory(movie)
    }
}