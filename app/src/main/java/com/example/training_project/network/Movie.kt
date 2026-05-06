package com.example.training_project.network

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>? = null
)

data class Genre(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null
)

data class CreditsResponse(
    @SerializedName("cast") val cast: List<Cast>? = null
)

data class Cast(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("character") val character: String? = null,
    @SerializedName("profile_path") val profilePath: String? = null
){
    fun getProfileUrl(size: String = "w185"): String {
        if (this.profilePath.isNullOrEmpty()) return ""
        return "https://image.tmdb.org/t/p/$size${this.profilePath}"
    }
}

data class ReviewsResponse(
    @SerializedName("results") val results: List<Review>? = null
)

data class AuthorDetails(
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("avatar_path") val avatarPath: String? = null,
    @SerializedName("rating") val rating: Double? = null
)

data class Review(
    @SerializedName("id") val id: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("author_details") val authorDetails: AuthorDetails? = null
) {
    fun getAvatarUrl(): String {
        val path = authorDetails?.avatarPath ?: return ""

        if (path.startsWith("/http")) {
            return path.substring(1)
        }
        return "https://image.tmdb.org/t/p/w200$path"
    }
}

enum class MovieGenre(val id: Int, val title: String) {
    ACTION(28, "Action"),
    ADVENTURE(12, "Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    FANTASY(14, "Fantasy"),
    HISTORY(36, "History"),
    HORROR(27, "Horror"),
    MUSIC(10402, "Music"),
    MYSTERY(9648, "Mystery"),
    ROMANCE(10749, "Romance"),
    SCIENCE_FICTION(878, "Science Fiction"),
    TV_MOVIE(10770, "TV Movie"),
    THRILLER(53, "Thriller"),
    WAR(10752, "War"),
    WESTERN(37, "Western");

    companion object {
        fun getTitleById(id: Int): String? {
            return values().find { it.id == id }?.title
        }
    }
}

data class Movie(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("genres") val genres: List<Genre>? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("credits") val credits: CreditsResponse? = null,
    @SerializedName("reviews") val reviews: ReviewsResponse? = null
) {
    fun getGenresText(): String {
        if (!genres.isNullOrEmpty()) return genres.joinToString(", ") { it.name ?: "" }
        if (!genreIds.isNullOrEmpty()) return genreIds.mapNotNull { MovieGenre.getTitleById(it) }.joinToString(", ")
        return "Unknown Genre"
    }

    fun getPosterUrl(size: String = "w500"): String {
        if (this.posterPath.isNullOrEmpty()) return ""
        return "https://image.tmdb.org/t/p/$size${this.posterPath}"
    }

    fun getBackdropUrl(size: String = "w780"): String {
        if (this.backdropPath.isNullOrEmpty()) return ""
        return "https://image.tmdb.org/t/p/$size${this.backdropPath}"
    }
}