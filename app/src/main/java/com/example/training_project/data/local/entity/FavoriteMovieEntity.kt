package com.example.training_project.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "genres") val genres: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "savedAt") val saveAt: Long,
    @ColumnInfo(name = "detailViewCount") val detailViewCount: Int,
)
