package com.js.movietrends.ui.home.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase) : ViewModel() {
    val upcomingUiState: Flow<PagingData<Movie>> = getUpcomingMoviesUseCase().cachedIn(viewModelScope)
}
