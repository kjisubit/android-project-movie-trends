package com.js.movietrends.viewmodel

import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ApiResultState
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import com.js.movietrends.fixture.movieSample
import com.js.movietrends.ui.home.weeklyspotlight.WeeklySpotlightViewModel
import com.js.movietrends.helper.MainDispatcherRule
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WeeklySpotlightViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getWeeklySpotlightedMovieUseCase: GetWeeklySpotlightedMovieUseCase

    @Test
    fun fetchMovie_loading_initialState() {
        // UseCase가 아무것도 반환하지 않는 상황 가정
        whenever(getWeeklySpotlightedMovieUseCase()).thenReturn(emptyFlow())

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightedMovieUseCase)

        // 초기 상태가 Loading인지 검증
        assertEquals(ApiResultState.Loading, viewModel.weeklySpotlightUiState.value)
    }

    @Test
    fun fetchMovie_success_updatesState() {
        // UseCase가 성공 결과를 반환하는 상황 가정
        whenever(getWeeklySpotlightedMovieUseCase()).thenReturn(
            flowOf(ApiResult.Success(movieSample))
        )

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightedMovieUseCase)

        // 상태가 Success로 업데이트되는지 검증
        assertEquals(ApiResultState.Success(movieSample), viewModel.weeklySpotlightUiState.value)
    }

    @Test
    fun fetchMovie_error_updatesState() {
        // UseCase가 에러를 반환하는 상황 가정
        val exception = RuntimeException("Network error")
        whenever(getWeeklySpotlightedMovieUseCase()).thenReturn(
            flowOf(ApiResult.Error(exception))
        )

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightedMovieUseCase)

        // 상태가 Error로 업데이트되는지 검증
        assertEquals(ApiResultState.Error(exception), viewModel.weeklySpotlightUiState.value)
    }
}
