package com.example.domain.model

enum class MovieCategory(
    val value: String
) {
    NOW_PLAYING("now_playing"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    UPCOMING("upcoming"),
    TRENDING("trending")
}
