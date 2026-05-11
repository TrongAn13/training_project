package com.example.training_project.domain.usecase

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository

class GetMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(category: String, page: Int=1): List<Movie>{
        return when(category){
            "popular" -> repository.getPopularMovies(page)
            "top_rated" -> repository.getTopRatedMovies(page)
            "up_coming" -> repository.getUpcomingMovies(page)
            "trending" -> repository.getTrendingMovies()
            else -> repository.getNowPlayingMovies(page)
        }
    }
}