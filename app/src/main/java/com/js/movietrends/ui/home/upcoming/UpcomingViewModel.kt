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
    private val _upcomingMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val upcomingMovies: StateFlow<PagingData<Movie>> = _upcomingMovies

    init {
        fetchUpcomingMovies()
    }

    private fun fetchUpcomingMovies() {
        viewModelScope.launch {
            useCases.getUpcomingMoviesUseCase()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _upcomingMovies.value = it
                }
        }
    }
}
