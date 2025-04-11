package com.js.movietrends.data.dto

import com.google.gson.annotations.SerializedName

data class DatesResponseDto(
    @SerializedName("maximum")
    val maximum: String? = null,
    @SerializedName("minimum")
    val minimum: String? = null
)