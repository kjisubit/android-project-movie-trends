package com.js.movietrends

import androidx.compose.ui.semantics.SemanticsActions.ScrollBy
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.testing.asSnapshot
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.usecase.UseCases
import com.js.movietrends.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
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
    lateinit var useCases: UseCases

    @Before
    fun setup() = hiltRule.inject()

    private val details by composeTestRule.stringResource(R.string.details)
    private val goToHome by composeTestRule.stringResource(R.string.go_to_home)
    private val weeklySpotlighted by composeTestRule.stringResource(R.string.weekly_spotlight)
    private val upcoming by composeTestRule.stringResource(R.string.upcoming)

    @Test
    fun homeScreen_isWeeklySpotlighted() {
        composeTestRule.apply {
            // Given: 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // When: 홈 화면 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // Then: 주간 추천 영화 탭이 활성화되어 있는지 확인
            onNodeWithText(weeklySpotlighted).assertIsSelected()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun homeScreen_showWeeklySpotlightedDetails() = runTest {
        // Given: spotlight 영화 정보 가져오기
        var movie: Movie? = null
        useCases.getWeeklySpotlightedMovieUseCase().collectLatest { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> movie = apiResult.data
                else -> fail("ApiResult was not Success: $apiResult")
            }
        }
        assertNotNull("Spotlighted movie should not be null", movie)
        val movieTitle = movie?.title.orEmpty()

        composeTestRule.apply {
            // Given: 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // When: 홈 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // Then: 상세 보기 버튼이 표시될 때까지 대기
            waitUntilNodeCount(
                timeoutMillis = 10_000,
                matcher = hasText(details),
                count = 1
            )

            // When: 상세 보기 버튼 클릭
            onNodeWithText(details).assertExists().performClick()

            // Then: 디테일 화면에서 영화 제목 확인
            onNodeWithText(movieTitle).assertExists()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun homeScreen_scrollUpcomingMovieList_navigateToDetail() {
        val itemIndex = 49

        // Given: 페이징 데이터에서 해당 인덱스 영화 정보 확보
        val (movieId, movieTitle) = runBlocking {
            val upcomingMovies = useCases.getUpcomingMoviesUseCase()
            val snapshot = upcomingMovies.asSnapshot {
                scrollTo(index = itemIndex)
            }

            // Then: 충분한 데이터가 있는지 확인
            assertTrue(
                "Movie list contains only ${snapshot.size} items, expected at least ${itemIndex + 1}",
                snapshot.size > itemIndex
            )

            val movie = snapshot[itemIndex]
            movie.id to movie.title.orEmpty()
        }

        composeTestRule.apply {
            // Given: 인트로 화면 진입 확인
            onNodeWithText(goToHome).assertExists()

            // When: 홈 화면 이동
            onNodeWithText(goToHome).performClick()

            // Then: 상세 보기 버튼이 표시될 때까지 대기
            waitUntilNodeCount(timeoutMillis = 20000, matcher = hasText(details), count = 1)

            // When: '개봉 예정' 탭 클릭
            onNodeWithText(upcoming).assertExists().performClick()

            // Then: 영화 리스트 노출 확인
            waitUntilNodeCount(
                timeoutMillis = 10000,
                matcher = hasTestTag("upcoming:movies"),
                count = 1
            )

            // When: 영화 리스트에서 해당 영화까지 스크롤
            onNodeWithTag("upcoming:movies")
                .performScrollToNode(hasTestTag("movieListItem:$movieId"))
                .fetchSemanticsNode()
                .apply {
                    val movieListItemNode =
                        onNodeWithTag("movieListItem:$movieId").fetchSemanticsNode()

                    // Then: 아이템 바닥이 보이도록 추가 스크롤
                    config[ScrollBy].action?.invoke(
                        0f,
                        (movieListItemNode.size.height - size.height).coerceAtLeast(0).toFloat()
                    )
                }

            // Then: 아이템 영화 제목 노출 확인
            onNodeWithText(movieTitle).assertExists()

            // When: 해당 영화 아이템 클릭
            onNodeWithText(movieTitle).performClick()

            // Then: 디테일 화면에서도 영화 제목 노출 확인
            onNodeWithText(movieTitle).assertExists()
        }
    }
}