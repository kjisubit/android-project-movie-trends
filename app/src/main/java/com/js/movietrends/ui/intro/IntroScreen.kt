package com.js.movietrends.ui.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.js.movietrends.R
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
            Text(stringResource(R.string.go_to_home))
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



