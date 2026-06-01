package com.example.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "movies",  primaryKeys = ["id", "category"])
data class MovieEntity(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "runtime") val runtime: Int,
    @ColumnInfo(name = "genres") val genres: String,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "page") val page : Int,
    @ColumnInfo(name = "position") val position: Int
)
