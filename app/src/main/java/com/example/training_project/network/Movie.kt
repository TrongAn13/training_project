package com.example.training_project.network
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)
data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
data class CreditsResponse(
    @SerializedName("cast") val cast: List<Cast>? = null
)

data class Cast(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String,
    @SerializedName("profile_path") val profilePath: String?
)

data class ReviewsResponse(
    @SerializedName("results") val results: List<Review>? = null
)

data class AuthorDetails(
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("avatar_path") val avatarPath: String?,
    @SerializedName("rating") val rating: Double?
)

data class Review(
    @SerializedName("id") val id: String,
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
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
data class Movie (

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
        if (!genres.isNullOrEmpty()) return genres.joinToString(", ") { it.name }
        if (!genreIds.isNullOrEmpty()) return genreIds.mapNotNull { genreMap[it] }
            .joinToString(", ")
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

    companion object {
        val genreMap = mapOf(
            28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy",
            80 to "Crime", 99 to "Documentary", 18 to "Drama", 10751 to "Family",
            14 to "Fantasy", 36 to "History", 27 to "Horror", 10402 to "Music",
            9648 to "Mystery", 10749 to "Romance", 878 to "Science Fiction",
            10770 to "TV Movie", 53 to "Thriller", 10752 to "War", 37 to "Western"
        )
    }
}