package com.example.training_project.domain.usecase

data class MovieUseCases(

    val getMovies: GetMoviesUseCase,

    val searchMovies: SearchMoviesUseCase,

    val getMovieDetails: GetMovieDetailsUseCase,

)