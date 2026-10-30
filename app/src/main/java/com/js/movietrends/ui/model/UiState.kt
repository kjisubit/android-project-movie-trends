package com.js.movietrends.ui.model

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
}