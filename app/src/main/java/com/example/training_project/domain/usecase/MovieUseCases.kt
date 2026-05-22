package com.example.training_project.domain.usecase

data class MovieUseCases(

    val getMovies: GetMoviesUseCase,

    val searchMovies: SearchMoviesUseCase,

    val getMovieDetails: GetMovieDetailsUseCase,

    val getCachedMovies: GetCachedMoviesUseCase,

    val refreshMovies: RefreshMoviesUseCase,

    val getSearchHistory: GetSearchHistoryUseCase,

    val saveSearchHistory: SaveSearchHistoryUseCase,

    val clearSearchHistory: ClearSearchHistoryUseCase,

    val getFavoriteMovies: GetFavoriteMoviesUseCase,

    val deleteFavoriteMovie: DeleteFavoriteMovieUseCase,

    val saveFavoriteMovie: SaveFavoriteMovieUseCase,

    val isMovieSaved: IsMovieSavedUseCase,

    val increaseDetailViewCount: IncreaseDetailViewCount
)
