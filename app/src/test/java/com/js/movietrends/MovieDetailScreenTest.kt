package com.js.movietrends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.js.movietrends.fixture.movieSample
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
    fun detailScreen_showSampleMovieData() {
        composeTestRule.apply {
            // 더미 데이터로 영화 화면 노출
            setContent {
                MovieTrendsTheme {
                    MovieDetailScreen(
                        movie = movieSample,
                        upPress = {}
                    )
                }
            }

            // 뒤로 가기 버튼 노출 확인
            onNodeWithTag("back_button").assertIsDisplayed()

            // 더미 데이터 텍스트 노출 확인
            onNodeWithText(movieSample.title.toString()).assertIsDisplayed()
            onNodeWithText(movieSample.overview.toString()).assertIsDisplayed()
        }
    }
}