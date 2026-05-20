package com.example.training_project.data.mapper

import com.example.training_project.data.local.entity.MovieEntity
import com.example.training_project.data.remote.DTO.CastDTO
import com.example.training_project.data.remote.DTO.MovieDTO
import com.example.training_project.data.remote.DTO.ReviewDTO
import com.example.training_project.domain.model.Cast
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.Review

object MovieMapper {
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

    fun MovieDTO.toEntity(category: String,page: Int = 1, position: Int): MovieEntity {
        return MovieEntity(
            id = this.id ?: 0,
            title = this.title ?: "Unknown",
            posterPath = this.posterPath ?: "",
            voteAverage = this.voteAverage ?: 0.0,
            releaseDate = this.releaseDate ?: "",
            overview = this.overview ?: "",
            runtime = this.runtime ?: 0,
            genres = this.genres?.joinToString(", ") { it.name ?: "" } 
                ?: this.genreIds?.mapNotNull { getGenreTitleById(it) }?.joinToString(", ") 
                ?: "",
            backdropPath = this.backdropPath ?: "",
            category = category,
            page = page,
            position = position
        )
    }

    fun MovieEntity.toDomain(): Movie {
        return Movie(
            id = this.id,
            title = this.title,
            posterUrl = "${IMAGE_BASE_URL}w500${this.posterPath}",
            rating = this.voteAverage,
            releaseDate = this.releaseDate,
            overview = this.overview,
            runtime = this.runtime,
            genres = this.genres,
            backdropUrl = "${IMAGE_BASE_URL}w780${this.backdropPath}"
        )
    }

    fun MovieDTO.toDomain(): Movie {
        return Movie(
            id = this.id ?: 0,
            title = this.title ?: "Unknown",
            posterUrl = "${IMAGE_BASE_URL}w500${this.posterPath}",
            rating = this.voteAverage ?: 0.0,
            releaseDate = this.releaseDate ?: "",
            overview = this.overview ?: "",
            runtime = this.runtime ?: 0,
            genres = this.genres?.joinToString(", ") { it.name ?: "" }
                ?: this.genreIds?.mapNotNull { getGenreTitleById(it) }?.joinToString(", ")
                ?: "",
            backdropUrl = "${IMAGE_BASE_URL}w780${this.backdropPath}",
            cast = this.credits?.cast?.map { it.toDomain() } ?: emptyList(),
            reviews = this.reviews?.results?.map { it.toDomain() } ?: emptyList()
        )
    }

    fun CastDTO.toDomain(): Cast {
        return Cast(
            id = this.id ?: 0,
            name = this.name ?: "Unknown",
            character = this.character ?: "",
            profileUrl = if (this.profilePath.isNullOrEmpty()) "" else "${IMAGE_BASE_URL}w185${this.profilePath}"
        )
    }

    fun ReviewDTO.toDomain(): Review {
        return Review(
            id = this.id ?: "",
            author = this.author ?: "Unknown",
            content = this.content ?: "",
            avatarUrl = getAvatarUrl(this.authorDetails?.avatarPath),
            rating = this.authorDetails?.rating ?: 0.0
        )
    }

    private fun getAvatarUrl(path: String?): String {
        if (path.isNullOrEmpty()) return ""
        if (path.startsWith("/http")) return path.substring(1)
        return "${IMAGE_BASE_URL}w200$path"
    }

    private fun getGenreTitleById(id: Int): String? {
        return when (id) {
            28 -> "Action"
            12 -> "Adventure"
            16 -> "Animation"
            35 -> "Comedy"
            80 -> "Crime"
            99 -> "Documentary"
            18 -> "Drama"
            10751 -> "Family"
            14 -> "Fantasy"
            36 -> "History"
            27 -> "Horror"
            10402 -> "Music"
            9648 -> "Mystery"
            10749 -> "Romance"
            878 -> "Science Fiction"
            10770 -> "TV Movie"
            53 -> "Thriller"
            10752 -> "War"
            37 -> "Western"
            else -> null
        }
    }
}