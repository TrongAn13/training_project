package com.example.training_project.data.network

object ApiEndpoints {
    const val POPULAR_MOVIES = "3/movie/popular"
    const val NOW_PLAYING_MOVIES = "3/movie/now_playing"
    const val UPCOMING_MOVIES = "3/movie/upcoming"
    const val TOP_RATED_MOVIES = "3/movie/top_rated"
    const val TRENDING_MOVIES = "3/trending/movie/day"
    const val SEARCH_MOVIES = "3/search/movie"
    const val MOVIE_DETAILS = "3/movie/{movie_id}"

    const val CREATE_REQUEST_TOKEN = "3/authentication/token/new"
    const val VALIDATE_WITH_LOGIN = "3/authentication/token/validate_with_login"
    const val CREATE_SESSION = "3/authentication/session/new"
}