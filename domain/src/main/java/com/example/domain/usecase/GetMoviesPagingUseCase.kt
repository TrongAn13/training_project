package com.example.domain.usecase

import com.example.domain.model.MovieCategory
import com.example.domain.repository.MovieRepository

class GetMoviesPagingUseCase(private val repository: MovieRepository) {
    operator fun invoke(category: MovieCategory) = repository.getMoviesPaging(category)
}