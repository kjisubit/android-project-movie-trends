package com.js.movietrends.ui.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.js.movietrends.ui.theme.MovieTrendsTheme

@Composable
fun IntroScreen(onNavigationToHome: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()

    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
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



