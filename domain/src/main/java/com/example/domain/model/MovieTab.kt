package com.example.domain.model
enum class MovieTab(val category: MovieCategory, val title: String){
    NOW_PLAYING(MovieCategory.NOW_PLAYING,"Now Playing"),
    UPCOMING(MovieCategory.UPCOMING,"Upcoming"),
    TOP_RATED(MovieCategory.TOP_RATED,"Top Rated"),
    POPULAR(MovieCategory.POPULAR,"Popular")
}