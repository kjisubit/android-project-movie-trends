package com.js.movietrends.domain.core

object Constants {
    const val BASIC_URL: String = "https://api.themoviedb.org/3/"
    const val POSTER_URL: String = "https://image.tmdb.org/t/p/"

    const val POSTER_SMALL = "w92"  // 가장 작은 썸네일 크기
    const val POSTER_MEDIUM = "w154"
    const val POSTER_LARGE = "w185"
    const val POSTER_XLARGE = "w342"
    const val POSTER_XXLARGE = "w500"  // 기본적으로 많이 사용하는 크기
    const val POSTER_FULL = "original"  // 원본 크기

    const val INTRO_ROUTE: String = "intro_route"
    const val MAIN_ROUTE: String = "main_route"
    const val NOW_PLAYING_ROUTE: String = "now_playing_route"
    const val POPULAR_ROUTE: String = "popular_route"
    const val UPCOMING_ROUTE: String = "upcoming_route"
}