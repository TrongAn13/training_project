package com.example.domain.usecase

import com.example.domain.repository.MovieRepository

class IsMovieSavedUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(movieId: Long): Boolean {
        return repository.isMovieSaved(movieId)
    }
}