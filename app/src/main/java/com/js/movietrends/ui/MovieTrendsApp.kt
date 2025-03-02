package com.js.movietrends.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.ui.detail.MovieDetailScreen
import com.js.movietrends.ui.home.HomeScreen
import com.js.movietrends.ui.intro.IntroScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
object Home

@Serializable
object Intro

@Serializable
data class MovieDetail(
    val movieJson: String
)

@Composable
fun MovieTrendsNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Intro,
    ) {
        composable<Home> {
            HomeScreen(
                onNavigationToMovieDetail = { movie ->
                    val movieString = Json.encodeToString(movie)
                    navController.navigate(route = MovieDetail(movieString))
                }
            )
        }
        composable<Intro> {
            IntroScreen(
                onNavigationToHome = {
                    navController.navigate(route = Home)
                }
            )
        }
        composable<MovieDetail> { backStackEntry ->
            val movieDetail: MovieDetail = backStackEntry.toRoute()
            val movie = Json.decodeFromString<Movie>(movieDetail.movieJson)
            MovieDetailScreen(movie)
        }
    }
}