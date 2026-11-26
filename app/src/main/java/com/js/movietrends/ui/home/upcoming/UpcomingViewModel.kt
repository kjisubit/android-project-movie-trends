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
    val upcomingUiState: Flow<PagingData<Movie>> = getUpcomingMoviesUseCase()
        // Pager가 만든 PagingData는 한 번만 구독 가능하기 때문에 화면이 재생성 될 때 다시 구독하면 크래시 발생
        // cachedIn()은 로드된 데이터를 viewModelScope 동안 보관함으로써 재구독 가능한 PagingData를 생성한다.
        .cachedIn(viewModelScope)
}
