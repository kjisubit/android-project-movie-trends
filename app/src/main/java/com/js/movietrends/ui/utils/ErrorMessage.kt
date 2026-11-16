package com.js.movietrends.ui.utils

import androidx.annotation.StringRes
import com.js.movietrends.R
import com.js.movietrends.domain.model.ErrorType

@StringRes
fun ErrorType.toMessageRes(): Int = when (this) {
    ErrorType.NETWORK -> R.string.error_network
    ErrorType.SERVER -> R.string.error_server
    ErrorType.NO_MOVIE -> R.string.error_no_movie
    ErrorType.UNKNOWN -> R.string.error_unknown
}
