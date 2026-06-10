package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.FavoriteMovieEntity

@Dao
interface FavoriteMovieDAO{
    @Query("SELECT * FROM favorite_movies ORDER BY DetailViewCount DESC, savedAt DESC")
    suspend fun getFavoriteMovies(): List<FavoriteMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isMovieSaved(movieId: Long): Boolean

    @Query("""
        UPDATE favorite_movies
        SET detailViewCount = detailViewCount + 1
        WHERE id = :movieId
    """)
    suspend fun increaseDetailViewCount(movieId: Long)
}

