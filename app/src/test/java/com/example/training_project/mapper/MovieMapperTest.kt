package com.example.training_project.mapper

import com.example.training_project.data.mapper.MovieMapper.toDomain
import com.example.training_project.data.mapper.MovieMapper.toEntity
import com.example.training_project.data.remote.DTO.MovieDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MovieMapperTest {

    @Test
    fun `movieDTO toDomain should return full image urls`() {
        val dto = MovieDTO(
            id = 100L,
            title = "Inception",
            posterPath = "/inception.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
            overview = "A thief who steals corporate secrets..."
        )
        val domain = dto.toDomain()

        assertEquals("https://image.tmdb.org/t/p/w500/inception.jpg", domain.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", domain.backdropUrl)
        assertEquals("Inception", domain.title)
    }

    @Test
    fun `movieDTO toEntity should map position and page correctly`() {
        val dto = MovieDTO(id = 1L, title = "Test", posterPath = "", voteAverage = 0.0, releaseDate = "", overview = "")

        val entity = dto.toEntity(category = "popular", page = 2, position = 5)

        assertEquals(2, entity.page)
        assertEquals(5, entity.position)
        assertEquals("popular", entity.category)
    }

    @Test
    fun `movieDTO with genreIds should map to correct genre names`() {
        val dto = MovieDTO(
            id = 1L, title = "Action Movie", posterPath = "", voteAverage = 0.0, releaseDate = "", overview = "",
            genreIds = listOf(28, 12)
        )
        val domain = dto.toDomain()
        assertEquals("Action, Adventure", domain.genres)
    }
}