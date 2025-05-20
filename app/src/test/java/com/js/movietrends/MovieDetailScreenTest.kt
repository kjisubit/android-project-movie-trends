package com.js.movietrends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.ui.detail.MovieDetailScreen
import com.js.movietrends.ui.theme.MovieTrendsTheme
import com.js.movietrends.uitesthiltmanifest.HiltComponentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class MovieDetailScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun detailScreen_showDummyMovieData() {
        composeTestRule.apply {
            // Given: 더미 데이터 준비
            val dummyMovie = SampleData.createDummyMovie()

            // When: 더미 데이터로 영화 화면 노출
            setContent {
                MovieTrendsTheme {
                    MovieDetailScreen(
                        movie = dummyMovie,
                        upPress = {}
                    )
                }
            }

            // Then: 뒤로 가기 버튼 노출 확인
            onNodeWithTag("back_button").assertIsDisplayed()

            // Then: 더미 데이터 텍스트 노출 확인
            onNodeWithText(dummyMovie.title.toString()).assertIsDisplayed()
            onNodeWithText(dummyMovie.overview.toString()).assertIsDisplayed()
        }
    }
}