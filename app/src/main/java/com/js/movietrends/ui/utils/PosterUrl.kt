package com.js.movietrends.ui.utils

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/"

/**
 * TMDB 포스터 이미지 크기. 화면마다 필요한 해상도가 달라 UI에서 선택한다.
 */
enum class PosterSize(val path: String) {
    XLARGE("w342"),
    XXLARGE("w500"),
    FULL("original"),
}

fun posterUrl(posterPath: String?, size: PosterSize): String? =
    posterPath?.let { "$POSTER_BASE_URL${size.path}$it" }
