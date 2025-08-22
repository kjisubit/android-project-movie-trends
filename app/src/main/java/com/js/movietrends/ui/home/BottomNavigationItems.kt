package com.js.movietrends.ui.home

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.js.movietrends.R
import com.js.movietrends.domain.core.Constants

data class BottomNavigationItem(
    val title: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val screenRoute: String = ""
)

fun getBottomNavigationItems(context: Context): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = context.getString(R.string.weekly_spotlight),
            icon = Icons.Filled.Star,
            screenRoute = NavigationScreens.WeeklySpotlight.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.now_playing),
            icon = Icons.Filled.PlayArrow,
            screenRoute = NavigationScreens.NowPlaying.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.upcoming),
            icon = Icons.Filled.DateRange,
            screenRoute = NavigationScreens.Upcoming.screenRoute
        )
    )
}

/**
 * Constants 문자열 상수만 가지고 navigation 구현할 시,
 * 해당 문자열 상수의 사용처가 명확하지 않을 수 있고, when 표현식의 완전성 검사 또한 불가한 이슈 발생.
 * 별도의 NavigationScreens 타입을 정의하여 개선
 */
sealed class NavigationScreens(val screenRoute: String) {
    data object WeeklySpotlight : NavigationScreens(Constants.WEEKLY_SPOTLIGHT_ROUTE)
    data object NowPlaying : NavigationScreens(Constants.NOW_PLAYING_ROUTE)
    data object Upcoming : NavigationScreens(Constants.UPCOMING_ROUTE)
}