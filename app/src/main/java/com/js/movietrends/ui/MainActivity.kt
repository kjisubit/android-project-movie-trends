package com.js.movietrends.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.js.movietrends.ui.theme.movietrendsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            movietrendsTheme {
                MovieTrendsNavHost()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieTrendsAppView() {
    movietrendsTheme {
        MovieTrendsNavHost()
    }
}

