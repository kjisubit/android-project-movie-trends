package com.js.movietrends.usecase

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import com.js.movietrends.fixture.movieSample
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetUpcomingMoviesUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var useCase: GetUpcomingMoviesUseCase

    @Before
    fun setup() {
        useCase = GetUpcomingMoviesUseCase(movieRepository)
    }

    @Test
    fun invoke_returnsMovieList() = runTest {
        // Repository가 영화 목록을 반환하는 상황 가정
        whenever(movieRepository.getUpcomingMovies()).thenReturn(
            flowOf(PagingData.from(listOf(movieSample)))
        )

        val snapshot = useCase().asSnapshot()

        // 영화 데이터가 올바르게 전달되는지 검증
        assertTrue(snapshot.isNotEmpty())
        assertEquals(movieSample.id, snapshot.first().id)
        assertEquals(movieSample.title, snapshot.first().title)
    }
}
