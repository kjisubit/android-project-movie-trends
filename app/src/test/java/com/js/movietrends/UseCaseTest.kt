package com.js.movietrends

import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class UseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var useCases: UseCases

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun useCase_weeklySpotlightedMovieExists() {
        // Given: 데이터 담을 변수 준비
        var movie: Movie? = null

        // When: 데이터 로드
        runBlocking {
            useCases.getWeeklySpotlightedMovieUseCase().collect { apiResult ->
                if (apiResult is ApiResult.Success) {
                    movie = apiResult.data
                }
            }
        }

        // Then: 유효한 영화 데이터 존재 여부 체크
        assertTrue(movie?.id != null)
    }

    @Test
    fun useCase_nowPlayingMovieListAvailable() {
        // Given: 무비 리스트에서 찾고자 하는 인덱스 준비
        val itemIndex = 49

        // When: 페이징 데이터 스크롤 하여, 탐색 대상의 id와 title 로드
        val movieSnapshot = runBlocking {
            val upcomingMovies = useCases.getUpcomingMoviesUseCase()
            upcomingMovies.asSnapshot {
                scrollTo(index = itemIndex)
            }
        }

        // Then: 유효한 무비 리스트가 존재하며, id와 title 정보 확인 가능 여부 체크
        assertTrue(movieSnapshot.isNotEmpty())
        assertNotNull(movieSnapshot[itemIndex].id)
        assertTrue(movieSnapshot[itemIndex].title != null)
    }
}