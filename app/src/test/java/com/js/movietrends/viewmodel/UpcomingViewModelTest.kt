package com.js.movietrends.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import com.js.movietrends.fixture.movieSample
import com.js.movietrends.ui.home.upcoming.UpcomingViewModel
import com.js.movietrends.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UpcomingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @Test
    fun fetchMovies_updatesState() = runTest(mainDispatcherRule.testDispatcher) {
        // UseCase가 데이터를 반환하는 상황 가정
        // cachedIn(viewModelScope)은 완료되지 않고 계속 살아있는 Flow를 요구하므로 Pager.flow로 stubbing
        whenever(getUpcomingMoviesUseCase()).thenReturn(
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    object : PagingSource<Int, Movie>() {
                        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
                            LoadResult.Page(
                                data = listOf(movieSample),
                                prevKey = null,
                                nextKey = null
                            )
                        override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
                    }
                }
            ).flow
        )

        val viewModel = UpcomingViewModel(getUpcomingMoviesUseCase)

        // PagingData는 직접 비교할 수 없으므로 asSnapshot()으로 리스트로 변환하여 검증
        val snapshot = viewModel.upcomingUiState.asSnapshot()
        assertTrue(snapshot.isNotEmpty())
        assertEquals(movieSample.id, snapshot.first().id)
        assertEquals(movieSample.title, snapshot.first().title)
    }
}
