package com.js.movietrends.ui.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.js.movietrends.R
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.ui.components.MovieTrendsScaffold
import com.js.movietrends.ui.home.nowplaying.NowPlayingScreen
import com.js.movietrends.ui.home.upcoming.UpcomingScreen
import com.js.movietrends.ui.home.weeklyspotlight.WeeklySpotlightScreen

@Composable
fun HomeScreen(onNavigationToMovieDetail: (Movie) -> Unit) {
    val context = LocalContext.current
    val bottomNavController = rememberNavController()

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val navigationItems = getBottomNavigationItems(context)
    val navigationSelectedItem = navigationItems
        .indexOfFirst { it.screenRoute == currentDestination }
        .takeIf { it != -1 } ?: 0

    MovieTrendsScaffold(
        bottomBar = {
            BottomNavigationBar(
                navigationItems = navigationItems,
                navigationSelectedItem = navigationSelectedItem,
                onClick = { index ->
                    bottomNavController.navigate(navigationItems[index].screenRoute) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavigationScreens.WeeklySpotlight.screenRoute,
        ) {
            composable(NavigationScreens.WeeklySpotlight.screenRoute) {
                WeeklySpotlightScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    onNavigationToMovieDetail = onNavigationToMovieDetail
                )
            }
            composable(NavigationScreens.NowPlaying.screenRoute) {
                NowPlayingScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    onNavigationToMovieDetail = onNavigationToMovieDetail
                )
            }
            composable(NavigationScreens.Upcoming.screenRoute) {
                UpcomingScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    onNavigationToMovieDetail = onNavigationToMovieDetail
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navigationItems: List<BottomNavigationItem>,
    navigationSelectedItem: Int,
    onClick: (Int) -> Unit
) {
    NavigationBar(
        containerColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.black)
    ) {
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                selected = index == navigationSelectedItem,
                label = {
                    Text(
                        text = navigationItem.title,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                },
                icon = {
                    Icon(
                        navigationItem.icon,
                        contentDescription = navigationItem.title,
                        tint = if (index == navigationSelectedItem) colorResource(id = R.color.black)
                        else colorResource(id = R.color.quick_silver),
                    )
                },
                onClick = {
                    onClick(index)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val navigationItems = getBottomNavigationItems(context)
    val navigationSelectedItem = 0

    BottomNavigationBar(
        navigationItems = navigationItems,
        navigationSelectedItem = navigationSelectedItem,
        onClick = { }
    )
}