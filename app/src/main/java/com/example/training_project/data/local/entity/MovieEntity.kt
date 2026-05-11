package com.example.training_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
    val releaseDate: String,
    val runtime: Int,
    val genres: String,
    val backdropPath: String,
    val overview: String,
    val category: String
)
