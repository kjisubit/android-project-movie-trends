package com.js.movietrends.presentation.features.main.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.js.movietrends.R
import com.js.movietrends.presentation.features.main.navigation.NavigationScreens
import com.js.movietrends.presentation.features.main.navigation.getBottomNavigationItems
import com.js.movietrends.presentation.features.now.view.NowPlayingSection
import com.js.movietrends.presentation.features.popular.view.PopularSection
import com.js.movietrends.presentation.features.upcoming.view.UpcomingSection

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ) {
                getBottomNavigationItems(context).forEachIndexed { index, navigationItem ->
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
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.screenRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemColors(
                            selectedIconColor = colorResource(id = R.color.transparent),
                            selectedTextColor = colorResource(id = R.color.transparent),
                            selectedIndicatorColor = colorResource(id = R.color.transparent),
                            unselectedIconColor = colorResource(id = R.color.transparent),
                            unselectedTextColor = colorResource(id = R.color.transparent),
                            disabledIconColor = colorResource(id = R.color.transparent),
                            disabledTextColor = colorResource(id = R.color.transparent),
                        ),
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.NowPlaying.screenRoute,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            composable(NavigationScreens.NowPlaying.screenRoute) {
                NowPlayingSection()
            }
            composable(NavigationScreens.Upcoming.screenRoute) {
                UpcomingSection()
            }
            composable(NavigationScreens.POPULAR.screenRoute) {
                PopularSection()
            }
        }
    }
}