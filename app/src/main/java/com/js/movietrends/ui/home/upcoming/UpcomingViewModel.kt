package com.js.movietrends.ui.home.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _upcomingUiState = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val upcomingUiState: StateFlow<PagingData<Movie>> = _upcomingUiState

    init {
        fetchUpcomingMovies()
    }

    private fun fetchUpcomingMovies() {
        viewModelScope.launch {
            useCases.getUpcomingMoviesUseCase()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _upcomingUiState.value = it
                }
        }
    }
}
