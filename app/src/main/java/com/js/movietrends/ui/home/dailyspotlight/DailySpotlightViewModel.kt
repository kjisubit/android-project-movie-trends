package com.js.movietrends.ui.home.dailyspotlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailySpotlightViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _bestRatedMovie = MutableStateFlow<ApiResult<Movie>>(ApiResult.Loading)
    val bestRatedMovie: StateFlow<ApiResult<Movie>> = _bestRatedMovie

    init {
        fetchFetchBestRatedMovie()
    }

    private fun fetchFetchBestRatedMovie() {
        viewModelScope.launch {
            useCases.getBestRatedMovieUseCase()
                .collectLatest {
                    _bestRatedMovie.value = it
                }
        }
    }
}