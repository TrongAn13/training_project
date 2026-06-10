package com.example.training_project.domain.usecase

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.MovieCategory
import com.example.training_project.domain.repository.MovieRepository
class GetMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(category: MovieCategory, page: Int = 1): List<Movie> {
        return when (category) {
            MovieCategory.POPULAR -> repository.getPopularMovies(page)
            MovieCategory.TOP_RATED -> repository.getTopRatedMovies(page)
            MovieCategory.UPCOMING -> repository.getUpcomingMovies(page)
            MovieCategory.TRENDING -> repository.getTrendingMovies()
            MovieCategory.NOW_PLAYING -> repository.getNowPlayingMovies(page)
        }
    }
}