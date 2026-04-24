package com.example.training_project.network
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)
data class Movie (

    @SerializedName("id") val id: Long = 0L,

    @SerializedName("title") val title: String? = null,

    @SerializedName("poster_path") val posterPath: String? = null

)