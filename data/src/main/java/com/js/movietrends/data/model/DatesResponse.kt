package com.js.movietrends.data.model

import com.google.gson.annotations.SerializedName

data class DatesResponse(
    @SerializedName("maximum")
    val maximum: String? = null,
    @SerializedName("minimum")
    val minimum: String? = null
)