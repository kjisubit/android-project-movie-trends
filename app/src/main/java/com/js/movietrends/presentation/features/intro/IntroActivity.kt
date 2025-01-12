package com.js.movietrends.presentation.features.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.js.movietrends.MainActivity
import com.js.movietrends.presentation.theme.movietrendsTheme


class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            movietrendsTheme {
                CenteredButtonWithText()
            }
        }

        goMain()
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        this.startActivity(intent)
    }
}


@Composable
private fun CenteredButtonWithText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(onClick = {

        }) {
            Text("Go to Main")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CenteredButtonWithTextPreview() {
    movietrendsTheme {
        CenteredButtonWithText()
    }
}