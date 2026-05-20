package com.example.training_project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.training_project.data.local.entity.MovieEntity

@Dao
interface MovieDAO{
    @Query("SELECT * FROM movies WHERE category = :category ORDER BY page ASC, position ASC")
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies WHERE category = :category")
    suspend fun deleteMoviesByCategory(category: String)
}
