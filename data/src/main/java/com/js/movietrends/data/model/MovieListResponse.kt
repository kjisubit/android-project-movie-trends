package com.js.movietrends.data.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("dates")
    val dates: DatesResponse? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<MovieResponse?>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)