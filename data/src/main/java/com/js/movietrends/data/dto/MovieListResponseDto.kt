package com.js.movietrends.data.dto

import com.google.gson.annotations.SerializedName

data class MovieListResponseDto(
    @SerializedName("dates")
    val datesResponseDto: DatesResponseDto? = null,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieResponseDto>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)