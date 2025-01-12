package com.js.movietrends.presentation.features.intro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.js.movietrends.presentation.features.intro.components.CenteredButtonWithText


@Composable
fun IntroScreen(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        CenteredButtonWithText(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
        )
    }
}



