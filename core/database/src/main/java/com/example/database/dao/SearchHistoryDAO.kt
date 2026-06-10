package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDAO{
    @Query("SELECT * FROM search_history ORDER BY savedAt DESC")
    suspend fun getSearchHistory(): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(movie: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun deleteHistory()
}