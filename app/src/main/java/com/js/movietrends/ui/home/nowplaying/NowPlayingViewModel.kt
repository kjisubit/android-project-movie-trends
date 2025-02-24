package com.js.movietrends.ui.home.nowplaying

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
class NowPlayingViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {
    private val _nowPlayingUiState = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val nowPlayingUiState: StateFlow<PagingData<Movie>> = _nowPlayingUiState

    init {
        fetchNowPlayingMovies()
    }

    private fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            useCases.getNowPlayingMoviesUseCase()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _nowPlayingUiState.value = it
                }
        }
    }
}