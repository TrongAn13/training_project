package com.example.training_project.data.network

import com.example.training_project.data.remote.DTO.CreateSessionRequest
import com.example.training_project.data.remote.DTO.LoginRequest
import com.example.training_project.data.remote.DTO.MovieDTO
import com.example.training_project.data.remote.DTO.MovieResponse
import com.example.training_project.data.remote.DTO.RequestTokenResponse
import com.example.training_project.data.remote.DTO.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

object ApiParams {
    const val MOVIE_DETAILS_APPEND = "credits,reviews"
}
interface TmdbApi {
    @GET(ApiEndpoints.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET(ApiEndpoints.NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET(ApiEndpoints.UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET(ApiEndpoints.TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET(ApiEndpoints.TRENDING_MOVIES)
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET(ApiEndpoints.SEARCH_MOVIES)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET(ApiEndpoints.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = ApiParams.MOVIE_DETAILS_APPEND
    ): MovieDTO

    @GET(ApiEndpoints.CREATE_REQUEST_TOKEN)
    suspend fun createRequestToken(): RequestTokenResponse

    @POST(ApiEndpoints.VALIDATE_WITH_LOGIN)
    suspend fun validateWithLogin(
        @Body request: LoginRequest
    ): RequestTokenResponse

    @POST(ApiEndpoints.CREATE_SESSION)
    suspend fun createSession(
        @Body request: CreateSessionRequest
    ): SessionResponse
}
