package com.js.movietrends.domain.usecase

import com.js.movietrends.domain.fixture.movieSample
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ErrorType
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(MockitoJUnitRunner::class)
class GetWeeklySpotlightUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var useCase: GetWeeklySpotlightUseCase

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val startDate = LocalDate.now().minusWeeks(2).format(dateFormatter)
    private val endDate = LocalDate.now().minusWeeks(1).format(dateFormatter)

    @Before
    fun setup() {
        useCase = GetWeeklySpotlightUseCase(movieRepository)
    }

    @Test
    fun invoke_success_returnsMovie() = runTest {
        // 지난 2주 ~ 1주 사이 인기 영화를 반환하는 상황 가정
        whenever(
            movieRepository.getWeeklySpotlight(
                startDate = startDate,
                endDate = endDate,
                sortBy = "popularity.desc"
            )
        ).thenReturn(ApiResult.Success(movieSample))

        val result = useCase()

        // 성공 결과와 영화 데이터가 올바르게 전달되는지 검증
        assertTrue(result is ApiResult.Success)
        assertEquals(movieSample.id, (result as ApiResult.Success).data.id)
        assertEquals(movieSample.title, result.data.title)
    }

    @Test
    fun invoke_error_propagatesError() = runTest {
        // Repository가 에러를 반환하는 상황 가정
        whenever(
            movieRepository.getWeeklySpotlight(
                startDate = startDate,
                endDate = endDate,
                sortBy = "popularity.desc"
            )
        ).thenReturn(ApiResult.Error(ErrorType.NETWORK))

        val result = useCase()

        // 에러가 그대로 전달되는지 검증
        assertEquals(ApiResult.Error(ErrorType.NETWORK), result)
    }
}
