package com.js.movietrends

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import com.js.movietrends.di.RepositoryModule
import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.fake.FakeMovieRepository
import com.js.movietrends.ui.MainActivity
import com.js.movietrends.util.stringResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class NavigationTest {

    @Module
    @InstallIn(SingletonComponent::class)
    object FakeRepositoryModule {
        @Provides
        @Singleton
        fun provideRepository(): MovieRepository = FakeMovieRepository()
    }

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorDrawable(Color.GRAY).asImage())
            .build()

        SingletonImageLoader.setSafe {
            ImageLoader.Builder(it)
                .components { add(engine) }
                .build()
        }
    }

    private val goToHome by composeTestRule.stringResource(R.string.go_to_home)
    private val weeklySpotlighted by composeTestRule.stringResource(R.string.weekly_spotlight)
    private val details by composeTestRule.stringResource(R.string.details)
    private val upcoming by composeTestRule.stringResource(R.string.upcoming)

    @Test
    fun intro_navigatesToHome() {
        composeTestRule.apply {
            // 인트로 화면 진입 여부 확인
            onNodeWithText(goToHome).assertExists()

            // 홈 화면 이동 버튼 클릭
            onNodeWithText(goToHome).performClick()

            // 주간 추천 영화 탭이 활성화되어 있는지 확인
            onNodeWithText(weeklySpotlighted).assertIsSelected()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun weeklySpotlight_navigatesToDetail() {
        composeTestRule.apply {
            // 홈 화면 이동
            onNodeWithText(goToHome).performClick()

            // 상세 보기 버튼이 표시될 때까지 대기
            waitUntilNodeCount(timeoutMillis = 5_000, matcher = hasText(details), count = 1)

            // 상세 보기 버튼 클릭
            onNodeWithText(details).performClick()

            // 디테일 화면 진입 확인
            onNodeWithTag("back_button").assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun upcoming_navigatesToDetail() {
        composeTestRule.apply {
            // 홈 화면 이동
            onNodeWithText(goToHome).performClick()

            // 홈 화면 렌더링 완료 대기
            waitUntilNodeCount(timeoutMillis = 5_000, matcher = hasText(details), count = 1)

            // 개봉 예정 탭 클릭
            onNodeWithText(upcoming).performClick()

            // 영화 리스트 노출 확인
            waitUntilNodeCount(
                timeoutMillis = 5_000,
                matcher = hasTestTag("upcoming:movies"),
                count = 1
            )

            // 영화 아이템 클릭
            onNodeWithTag("movieListItem:${FakeMovieRepository.MOVIE_ID}").performClick()

            // 디테일 화면 진입 확인
            onNodeWithTag("back_button").assertIsDisplayed()
        }
    }
}
