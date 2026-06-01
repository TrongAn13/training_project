package com.example.domain.usecase

import com.example.domain.repository.MovieRepository

class IncreaseDetailViewCount(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Long) {
        repository.increaseDetailViewCount(movieId)
    }
}