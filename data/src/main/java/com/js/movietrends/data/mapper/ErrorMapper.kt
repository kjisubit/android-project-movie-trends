package com.js.movietrends.data.mapper

import com.js.movietrends.domain.model.ErrorType
import retrofit2.HttpException
import java.io.IOException

internal fun Throwable.toErrorType(): ErrorType = when (this) {
    is IOException -> ErrorType.NETWORK
    is HttpException -> ErrorType.SERVER
    is NoSuchElementException -> ErrorType.NO_MOVIE
    else -> ErrorType.UNKNOWN
}
