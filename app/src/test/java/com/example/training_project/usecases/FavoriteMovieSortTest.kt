package com.example.training_project.usecases

import com.example.database.entity.FavoriteMovieEntity
import junit.framework.TestCase
import org.junit.Test

class FavoriteMovieSortTest {
    @Test
    fun  getFavoriteMovies_returnSortedMoviesByDetailViewCount() {
        val movies = listOf(
            FavoriteMovieEntity(
                id = 1,
                title = "Movie 1",
                posterPath = "poster1.jpg",
                voteAverage = 8.5,
                releaseDate = "2023-01-01",
                genres = "Action, Adventure",
                overview = "Overview 1",
                saveAt = 1672531200,
                detailViewCount = 5
            ),
            FavoriteMovieEntity(
                id = 2,
                title = "Movie 2",
                posterPath = "poster2.jpg",
                voteAverage = 9.5,
                releaseDate = "2023-02-01",
                genres = "Drama, Comedy",
                overview = "Overview 2",
                saveAt = 167,
                detailViewCount = 3
            ),
        )
        val result = movies.sortedWith ( compareByDescending<FavoriteMovieEntity> {
            it.detailViewCount
            }.thenByDescending { it.saveAt }
        )
        TestCase.assertEquals(2L, result[0].id)
        TestCase.assertEquals(1L, result[1].id)
    }
    @Test
    fun `favorite movies with the same detailViewCount are sorted by saveAt in descending`(){
        val movies = listOf(
            FavoriteMovieEntity(
                id = 1,
                title = "Movie 1",
                posterPath = "poster1.jpg",
                voteAverage = 5.5,
                releaseDate = "2023-01-01",
                genres = "Action, Adventure",
                overview = "Overview 1",
                saveAt = 1672531200,
                detailViewCount = 5
            ),
            FavoriteMovieEntity(
                id = 2,
                title = "Movie 2",
                posterPath = "poster2.jpg",
                voteAverage = 9.5,
                releaseDate = "2023-02-01",
                genres = "Drama, Comedy",
                overview = "Overview 2",
                saveAt = 1672531300,
                detailViewCount = 5
            ),
        )
        val result = movies.sortedWith ( compareByDescending<FavoriteMovieEntity> {
            it.detailViewCount
            }.thenByDescending { it.saveAt }
        )
        TestCase.assertEquals(2L, result[0].id)
        TestCase.assertEquals(1L, result[1].id)
        }
    }