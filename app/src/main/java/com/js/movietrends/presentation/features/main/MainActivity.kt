package com.js.movietrends.presentation.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.js.movietrends.presentation.features.main.view.MainScreen
import com.js.movietrends.presentation.theme.movietrendsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            movietrendsTheme {
                MainScreen()
            }
        }
    }
}
