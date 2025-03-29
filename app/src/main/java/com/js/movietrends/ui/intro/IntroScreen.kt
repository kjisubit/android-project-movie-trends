package com.js.movietrends.ui.intro

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import com.js.movietrends.ui.components.MovieTrendsSurface
import com.js.movietrends.ui.theme.MovieTrendsTheme

@Composable
fun IntroScreen(onNavigationToHome: () -> Unit) {
    MovieTrendsSurface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            MovieTrendsButton(
                onClick = {
                    onNavigationToHome()
                }
            ) {
                Text(text = stringResource(R.string.go_to_home))
            }
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun IntroScreenPreview() {
    MovieTrendsTheme {
        IntroScreen(onNavigationToHome = {})
    }
}
