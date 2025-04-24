package com.js.movietrends.domain.model

sealed class ApiResultState<out T> {
    data class Success<out T>(val data: T) : ApiResultState<T>()
    data class Error(val exception: Throwable) : ApiResultState<Nothing>()
    data object Loading : ApiResultState<Nothing>()
}