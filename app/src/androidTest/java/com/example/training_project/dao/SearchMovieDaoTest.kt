package com.example.training_project.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.training_project.data.local.AppDatabase
import com.example.training_project.data.local.dao.SearchHistoryDAO
import com.example.training_project.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMovieDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: SearchHistoryDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.searchHistoryDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetSearchHistoryShouldReturnCorrectOrder() = runBlocking {
        val movie1 = createFakeSearchEntity(id = 1, title = "Old Search", timestamp = 1000L)
        val movie2 = createFakeSearchEntity(id = 2, title = "New Search", timestamp = 2000L)

        dao.insertHistory(movie1)
        dao.insertHistory(movie2)

        val result = dao.getSearchHistory()

        assertEquals(2, result.size)
        assertEquals(2L, result[0].id)
        assertEquals("New Search", result[0].title)
    }

    @Test
    fun deleteHistoryShouldClearAllRecords() = runBlocking {
        dao.insertHistory(createFakeSearchEntity(id = 1, title = "Movie 1"))
        dao.insertHistory(createFakeSearchEntity(id = 2, title = "Movie 2"))

        dao.deleteHistory()

        val result = dao.getSearchHistory()
        assertEquals(0, result.size)
    }

    @Test
    fun insertDuplicateIdShouldReplaceOldRecord() = runBlocking {
        val movie1 = createFakeSearchEntity(id = 1, title = "Original Title")
        dao.insertHistory(movie1)

        val movie2 = createFakeSearchEntity(id = 1, title = "Updated Title")
        dao.insertHistory(movie2)

        val result = dao.getSearchHistory()
        assertEquals(1, result.size)
        assertEquals("Updated Title", result[0].title)
    }

    private fun createFakeSearchEntity(
        id: Long,
        title: String,
        timestamp: Long = System.currentTimeMillis()
    ): SearchHistoryEntity {
        return SearchHistoryEntity(
            id = id,
            title = title,
            posterPath = "/path.jpg",
            voteAverage = 7.5,
            releaseDate = "2024",
            genres = "Drama",
            overview = "Overview",
            saveAt = timestamp
        )
    }
}
