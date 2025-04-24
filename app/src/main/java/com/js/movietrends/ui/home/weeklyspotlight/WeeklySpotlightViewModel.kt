package com.js.movietrends.ui.home.weeklyspotlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ApiResultState
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklySpotlightViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _weeklySpotlightUiState =
        MutableStateFlow<ApiResultState<Movie>>(ApiResultState.Loading)
    val weeklySpotlightUiState: StateFlow<ApiResultState<Movie>> = _weeklySpotlightUiState

    init {
        fetchWeeklySpotlightMovie()
    }

    private fun fetchWeeklySpotlightMovie() {
        _weeklySpotlightUiState.value = ApiResultState.Loading
        viewModelScope.launch {
            useCases.getWeeklySpotlightedMovieUseCase()
                .collectLatest { result ->
                    _weeklySpotlightUiState.value = when (result) {
                        is ApiResult.Success -> ApiResultState.Success(result.data)
                        is ApiResult.Error -> ApiResultState.Error(result.exception)
                    }
                }
        }
    }
}