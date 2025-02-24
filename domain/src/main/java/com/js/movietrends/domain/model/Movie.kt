package com.js.movietrends.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
    val voteCount: Int?
)