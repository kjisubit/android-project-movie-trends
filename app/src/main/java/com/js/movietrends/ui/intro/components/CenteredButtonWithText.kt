package com.js.movietrends.ui.intro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.ui.theme.movietrendsTheme

@Composable
fun CenteredButtonWithText(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = {
            navController.navigate(Constants.MAIN_ROUTE)
        }) {
            Text("Go to Main")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CenteredButtonWithTextPreview() {
    val navController = rememberNavController()
    movietrendsTheme {
        CenteredButtonWithText(navController = navController)
    }
}