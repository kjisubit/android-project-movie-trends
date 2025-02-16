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
            title = context.getString(R.string.common_best_rated),
            icon = Icons.Filled.Star,
            screenRoute = NavigationScreens.BestRated.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.common_now_playing),
            icon = Icons.Filled.PlayArrow,
            screenRoute = NavigationScreens.NowPlaying.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.common_upcoming),
            icon = Icons.Filled.DateRange,
            screenRoute = NavigationScreens.Upcoming.screenRoute
        ),
    )
}

sealed class NavigationScreens(val screenRoute: String) {
    data object BestRated : NavigationScreens(Constants.BEST_RATED_ROUTE)
    data object NowPlaying : NavigationScreens(Constants.NOW_PLAYING_ROUTE)
    data object Upcoming : NavigationScreens(Constants.UPCOMING_ROUTE)
}