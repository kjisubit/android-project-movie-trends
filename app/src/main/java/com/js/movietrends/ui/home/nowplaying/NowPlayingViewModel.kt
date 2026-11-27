package com.js.movietrends.ui.home.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.GetNowPlayingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase) : ViewModel() {
    val nowPlayingUiState: Flow<PagingData<Movie>> = getNowPlayingMoviesUseCase().cachedIn(viewModelScope)
}
