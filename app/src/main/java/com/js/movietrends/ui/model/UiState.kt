package com.js.movietrends.ui.model

import com.js.movietrends.domain.model.ErrorType

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val type: ErrorType) : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
}
