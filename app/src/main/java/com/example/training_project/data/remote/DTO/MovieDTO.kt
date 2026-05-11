package com.example.training_project.data.remote.DTO

import com.google.gson.annotations.SerializedName

data class GenreDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null
)

data class CreditsResponse(
    @SerializedName("cast") val cast: List<CastDTO>? = null
)

data class CastDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("character") val character: String? = null,
    @SerializedName("profile_path") val profilePath: String? = null
)

data class ReviewsResponse(
    @SerializedName("results") val results: List<ReviewDTO>? = null
)

data class AuthorDetailsDTO(
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("avatar_path") val avatarPath: String? = null,
    @SerializedName("rating") val rating: Double? = null
)

data class ReviewDTO(
    @SerializedName("id") val id: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("author_details") val authorDetails: AuthorDetailsDTO? = null
)

data class MovieDTO(
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("genres") val genres: List<GenreDTO>? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("credits") val credits: CreditsResponse? = null,
    @SerializedName("reviews") val reviews: ReviewsResponse? = null
)

data class MovieResponse(
    @SerializedName("results") val results: List<MovieDTO>? = null
)
