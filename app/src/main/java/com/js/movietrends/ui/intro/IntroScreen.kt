package com.js.movietrends.ui.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.js.movietrends.ui.components.MovieTrendsButton
import com.js.movietrends.ui.theme.MovieTrendsTheme

@Composable
fun IntroScreen(onNavigationToHome: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()

    ) {
        MovieTrendsButton(
            onClick = {
                onNavigationToHome()
            }) {
            Text("Go to Main")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MovieTrendsTheme {
        IntroScreen(onNavigationToHome = {})
    }
}



