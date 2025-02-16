package com.js.movietrends.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.js.movietrends.R
import com.js.movietrends.ui.home.dailyspotlight.DailySpotlight
import com.js.movietrends.ui.home.nowplaying.NowPlaying
import com.js.movietrends.ui.home.upcoming.Upcoming

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val navigationItems = getBottomNavigationItems(context)
    val navigationSelectedItem =
        navigationItems.indexOfFirst { it.screenRoute == currentDestination }
            .takeIf { it != -1 } ?: 0

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ) {
                navigationItems.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
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
                            navController.navigate(navigationItem.screenRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.BestRated.screenRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationScreens.BestRated.screenRoute) {
                DailySpotlight()
            }
            composable(NavigationScreens.NowPlaying.screenRoute) {
                NowPlaying()
            }
            composable(NavigationScreens.Upcoming.screenRoute) {
                Upcoming()
            }
        }
    }
}