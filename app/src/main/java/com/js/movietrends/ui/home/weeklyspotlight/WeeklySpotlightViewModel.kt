package com.js.movietrends.ui.home.weeklyspotlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.ui.model.UiState
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklySpotlightViewModel @Inject constructor(
    private val getWeeklySpotlightedMovieUseCase: GetWeeklySpotlightedMovieUseCase
) : ViewModel() {

    private val _weeklySpotlightUiState =
        MutableStateFlow<UiState<Movie>>(UiState.Loading)
    val weeklySpotlightUiState: StateFlow<UiState<Movie>> = _weeklySpotlightUiState

    init {
        fetchWeeklySpotlightMovie()
    }

    private fun fetchWeeklySpotlightMovie() {
        _weeklySpotlightUiState.value = UiState.Loading
        viewModelScope.launch {
            getWeeklySpotlightedMovieUseCase()
                .collectLatest { result ->
                    _weeklySpotlightUiState.value = when (result) {
                        is ApiResult.Success -> UiState.Success(result.data)
                        is ApiResult.Error -> UiState.Error(result.exception)
                    }
                }
        }
    }
}