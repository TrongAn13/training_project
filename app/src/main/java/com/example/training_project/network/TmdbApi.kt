package com.example.training_project.network

import retrofit2.http.GET

interface TmdbApi {
    @GET("3/movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieResponse
    @GET("3/movie/now_playing?language=en-US&page=1")
    suspend fun getNowPlayingMovies(): MovieResponse
    @GET("3/movie/upcoming?language=en-US&page=1")
    suspend fun getUpcomingMovies(): MovieResponse
    @GET("3/movie/top_rated?language=en-US&page=1")
    suspend fun getTopRatedMovies(): MovieResponse
    @GET("3/trending/movie/day?language=en-US")
    suspend fun getTrendingMovies(): MovieResponse

}