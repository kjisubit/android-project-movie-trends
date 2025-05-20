package com.js.movietrends

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UseCaseUnitTest {

    @Mock
    private lateinit var getWeeklySpotlightedMovieUseCase: GetWeeklySpotlightedMovieUseCase

    @Mock
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @Before
    fun setup() {
        getWeeklySpotlightedMovieUseCase = mock()
        getUpcomingMoviesUseCase = mock()

        whenever(getWeeklySpotlightedMovieUseCase()).thenReturn(
            flow {
                emit(
                    ApiResult.Success(
                        SampleData.createDummyMovie(
                            id = 123,
                            title = "Mock Movie"
                        )
                    )
                )
            }
        )

        whenever(getUpcomingMoviesUseCase()).thenReturn(
            flow {
                emit(
                    PagingData.from(
                        List(100) { index ->
                            SampleData.createDummyMovie(
                                id = index,
                                title = "Movie $index"
                            )
                        }
                    )
                )
            }
        )
    }

    @Test
    fun useCase_weeklySpotlightedMovieExists() = runTest {
        // Given: spotlighted 영화 정보를 반환하는 유스케이스가 준비됨
        var movie: Movie? = null

        // When: 유스케이스를 호출하여 데이터를 수집함
        getWeeklySpotlightedMovieUseCase().collect { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> movie = apiResult.data
                else -> fail("ApiResult was not Success: $apiResult")
            }
        }

        // Then: 반환된 영화 정보가 기대한 값과 일치하는지 검증함
        assertNotNull(movie?.id)
        assertEquals(123, movie?.id)
        assertEquals("Mock Movie", movie?.title)
    }

    @Test
    fun useCase_nowPlayingMovieListAvailable() = runTest {
        // Given: 무비 리스트에서 찾고자 하는 인덱스 준비
        val itemIndex = 49

        // When: 페이징 데이터 스크롤 하여, 탐색 대상의 id와 title 로드
        val movieSnapshot = getUpcomingMoviesUseCase().asSnapshot {
            scrollTo(index = itemIndex)
        }

        // Then: 리스트가 충분한지 확인 후 아이템 검증
        assertTrue("Movie list is empty", movieSnapshot.isNotEmpty())
        assertTrue(
            "Movie list has only ${movieSnapshot.size} items, expected at least ${itemIndex + 1}",
            movieSnapshot.size > itemIndex
        )

        val movie = movieSnapshot[itemIndex]
        assertNotNull("Movie ID should not be null", movie.id)
        assertNotNull("Movie title should not be null", movie.title)
    }
}