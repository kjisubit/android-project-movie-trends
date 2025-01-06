package com.js.movietrends.presentation.features.main.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.js.movietrends.R
import com.js.movietrends.utils.Constants

data class BottomNavigationItem(
    val title: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val screenRoute: String = ""
)

fun getBottomNavigationItems(context: Context): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = context.getString(R.string.common_currently_playing),
            icon = Icons.Filled.DateRange,
            screenRoute = NavigationScreens.CurrentPlaying.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.common_upcoming),
            icon = Icons.Filled.Star,
            screenRoute = NavigationScreens.Upcoming.screenRoute
        ),
        BottomNavigationItem(
            title = context.getString(R.string.common_popular),
            icon = Icons.Filled.Favorite,
            screenRoute = NavigationScreens.POPULAR.screenRoute
        ),
    )
}

sealed class NavigationScreens(val screenRoute: String) {
    data object CurrentPlaying : NavigationScreens(Constants.CURRENTLY_PLAYING_ROUTE)
    data object Upcoming : NavigationScreens(Constants.UPCOMING_ROUTE)
    data object POPULAR : NavigationScreens(Constants.POPULAR_ROUTE)
}