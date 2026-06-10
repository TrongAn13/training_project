package com.example.training_project.domain.usecase

import com.example.training_project.domain.repository.MovieRepository

class IsMovieSavedUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(movieId: Long): Boolean {
        return repository.isMovieSaved(movieId)
    }
}