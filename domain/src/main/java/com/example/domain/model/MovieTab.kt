package com.example.domain.model
enum class MovieTab(val category: MovieCategory){
    NOW_PLAYING(MovieCategory.NOW_PLAYING),
    UPCOMING(MovieCategory.UPCOMING),
    TOP_RATED(MovieCategory.TOP_RATED),
    POPULAR(MovieCategory.POPULAR)
}