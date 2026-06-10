package com.example.training_project.domain.model

data class Genre(
    val id: Int,
    val name: String
)

data class Cast(
    val id: Long,
    val name: String,
    val character: String,
    val profileUrl: String
)

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val avatarUrl: String,
    val rating: Double
)

data class Movie(
    val id: Long,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val releaseDate: String,
    val overview: String,
    val runtime: Int = 0,
    val genres: String = "",
    val backdropUrl: String = "",
    val cast: List<Cast> = emptyList(),
    val reviews: List<Review> = emptyList()
)
