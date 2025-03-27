package com.js.movietrends

import android.util.Log
import androidx.compose.ui.semantics.SemanticsActions.ScrollBy
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class NavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var movieRepository: MovieRepository

    @Before
    fun setup() = hiltRule.inject()

    private val details by composeTestRule.stringResource(R.string.details)
    private val goToHome by composeTestRule.stringResource(R.string.go_to_home)
    private val nowPlaying by composeTestRule.stringResource(R.string.now_playing)
    private val weeklySpotlighted by composeTestRule.stringResource(R.string.weekly_spotlight)
    private val upcoming by composeTestRule.stringResource(R.string.upcoming)

    @Test
    fun homeScreen_isWeeklySpotlighted() {
        composeTestRule.apply {
            // Given: 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // When: 홈 화면 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // Then: 주간 추천 영화 활성화 여부 확인
            onNodeWithText(weeklySpotlighted).assertIsSelected()

            // composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        }
    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun homeScreen_showWeeklySpotlightedDetails() {
        composeTestRule.apply {
            // Given: 주간 추천 영화 데이터 확인
            lateinit var movie: Movie
            runBlocking {
                movieRepository.getWeeklySpotlightedMovie().collect { apiResult ->
                    if (apiResult is ApiResult.Success) {
                        movie = apiResult.data
                    }
                }
            }
            val movieTitle = movie.title.toString()

            // Given: 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // When: 홈 화면 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // Then: 이미지 포스터 다운로드 후, 상세 보기 버튼 노출되는 것 확인
            waitUntilNodeCount(timeoutMillis = 5000, matcher = hasText(details), count = 1)

            // When: 상세 보기 버튼 클릭
            onNodeWithText(details).assertExists().performClick()

            // Then: 디테일 화면에서 영화 제목 노출되는 것 확인
            onNodeWithText(movieTitle).assertExists()
        }
    }

    @Test
    fun homeScreen_showUpcomingMovieList() {
        composeTestRule.apply {
            // Given: 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // Given: 영화 목록에서 특정 인덱스에 위치한 데이터 확인
            val itemIndex = 19
            val movieSnapshot = runBlocking {
                val upcomingMovies = movieRepository.getUpcomingMovies()
                upcomingMovies.asSnapshot {
                    scrollTo(index = itemIndex)
                }
            }
            val movieId = movieSnapshot[itemIndex].id
            val movieTitle = movieSnapshot[itemIndex].title.toString()

            // When: 홈 화면 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // And: 개봉 예정 영화 이동 버튼 클릭
            onNodeWithText(upcoming).performClick()

            // And: 아이템 보이는 위치까지 스크롤
            onNodeWithTag("upcoming:movies")
                .performScrollToNode(hasTestTag("movieListItem:$movieId"))
                .fetchSemanticsNode()
                .apply {
                    val movieListItemNode =
                        onNodeWithTag("movieListItem:$movieId").fetchSemanticsNode()
                    // 아이템 크기가 부모 노드 보다 큰 케이스 대비해 아이템 바닥이 보일 때까지 추가 스크롤
                    config[ScrollBy].action?.invoke(
                        0f,
                        (movieListItemNode.size.height - size.height).coerceAtLeast(0).toFloat()
                    )
                }

            // Then: 아이템 영화 제목의 노출 확인
            Log.d("homeScreen_showUpcomingMovieList", movieTitle)
            onNodeWithText(movieTitle).assertExists()

            // When: 아이템 클릭
            onNodeWithText(movieTitle).performClick()

            // Then: 디테일 화면의 영화 제목 노출 여부 확인
            onNodeWithText(movieTitle).assertExists()
        }
    }
}