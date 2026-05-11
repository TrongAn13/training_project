package com.example.training_project.domain.usecase

import com.example.training_project.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) =
        repository.getMovieDetails(movieId)
}
