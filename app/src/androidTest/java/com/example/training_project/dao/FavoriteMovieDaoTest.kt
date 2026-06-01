package com.example.training_project.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.database.AppDatabase
import com.example.database.dao.FavoriteMovieDAO
import com.example.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteMovieDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: FavoriteMovieDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.favoriteMovieDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getFavoriteMovies_shouldSortBySavedAtDescThenDetailViewCountDesc() = runTest {
        val movie1 = createFakeMovie(
            id = 1L,
            title = "Movie Low View",
            views = 10,
            savedAt = 1000L
        )

        val movie2 = createFakeMovie(
            id = 2L,
            title = "Movie High View",
            views = 100,
            savedAt = 1000L
        )

        dao.saveMovies(movie1)
        dao.saveMovies(movie2)

        val result = dao.getFavoriteMovies()

        assertEquals(2, result.size)
        assertEquals(2L, result[0].id)
        assertEquals("Movie High View", result[0].title)
    }

    @Test
    fun increaseDetailViewCount_shouldUpdateViewsInDatabase() = runTest {
        val movie = createFakeMovie(
            id = 5L,
            title = "Test View",
            views = 0
        )
        dao.saveMovies(movie)
        dao.increaseDetailViewCount(5L)
        val updatedMovie = dao.getFavoriteMovies().find { it.id == 5L }
        assertEquals(1, updatedMovie?.detailViewCount)
    }

    @Test
    fun deleteMovie_shouldRemoveMovieFromDatabase() = runTest {
        val movie = createFakeMovie(
            id = 10L,
            title = "To Be Deleted"
        )

        dao.saveMovies(movie)
        assertTrue(dao.isMovieSaved(10L))
        dao.deleteMovie(10L)
        val isExist = dao.isMovieSaved(10L)
        assertFalse(isExist)
    }

    private fun createFakeMovie(
        id: Long,
        title: String,
        views: Int = 0,
        savedAt: Long = 1000L
    ): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = id,
            title = title,
            posterPath = "/path.jpg",
            voteAverage = 8.0,
            releaseDate = "2024",
            genres = "Action",
            overview = "Overview",
            saveAt = savedAt,
            detailViewCount = views
        )
    }
}