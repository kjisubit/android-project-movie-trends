package com.js.movietrends.viewmodel

import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ErrorType
import com.js.movietrends.ui.model.UiState
import com.js.movietrends.domain.usecase.GetWeeklySpotlightUseCase
import com.js.movietrends.fixture.movieSample
import com.js.movietrends.ui.home.weeklyspotlight.WeeklySpotlightViewModel
import com.js.movietrends.helper.MainDispatcherRule
import kotlinx.coroutines.awaitCancellation
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.wheneverBlocking

@RunWith(MockitoJUnitRunner::class)
class WeeklySpotlightViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getWeeklySpotlightUseCase: GetWeeklySpotlightUseCase

    @Test
    fun fetchMovie_loading_initialState() {
        // UseCase가 결과를 반환하지 않고 계속 대기하는 상황 가정
        wheneverBlocking { getWeeklySpotlightUseCase() }.doSuspendableAnswer { awaitCancellation() }

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightUseCase)

        // 초기 상태가 Loading인지 검증
        assertEquals(UiState.Loading, viewModel.weeklySpotlightUiState.value)
    }

    @Test
    fun fetchMovie_success_updatesState() {
        // UseCase가 성공 결과를 반환하는 상황 가정
        wheneverBlocking { getWeeklySpotlightUseCase() }.thenReturn(ApiResult.Success(movieSample))

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightUseCase)

        // 상태가 Success로 업데이트되는지 검증
        assertEquals(UiState.Success(movieSample), viewModel.weeklySpotlightUiState.value)
    }

    @Test
    fun fetchMovie_error_updatesState() {
        // UseCase가 에러를 반환하는 상황 가정
        wheneverBlocking { getWeeklySpotlightUseCase() }.thenReturn(ApiResult.Error(ErrorType.NETWORK))

        val viewModel = WeeklySpotlightViewModel(getWeeklySpotlightUseCase)

        // 상태가 Error로 업데이트되는지 검증
        assertEquals(UiState.Error(ErrorType.NETWORK), viewModel.weeklySpotlightUiState.value)
    }
}
