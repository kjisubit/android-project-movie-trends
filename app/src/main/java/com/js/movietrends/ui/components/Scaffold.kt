package com.js.movietrends.ui.components

import android.content.res.Resources
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.js.movietrends.model.SnackbarManager
import com.js.movietrends.ui.theme.MovieTrendsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Material의 Scaffold를 감싸고 MovieTrendsTheme 색상을 설정
 */
@Composable
fun MovieTrendsScaffold(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topBar: @Composable (() -> Unit) = {},
    bottomBar: @Composable (() -> Unit) = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable (() -> Unit) = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    backgroundColor: Color = MovieTrendsTheme.colors.uiBackground,
    contentColor: Color = MovieTrendsTheme.colors.textSecondary,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = {
            snackbarHost(snackBarHostState)
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = backgroundColor,
        contentColor = contentColor,
        content = content
    )
}

/**
 * MovieTrendsScaffoldState의 인스턴스를 생성하고 기억
 */
@Composable
fun rememberMovieTrendsScaffoldState(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MovieTrendsScaffoldState =
    remember(snackBarHostState, snackbarManager, resources, coroutineScope) {
        MovieTrendsScaffoldState(snackBarHostState, snackbarManager, resources, coroutineScope)
    }

/**
 * ScaffoldState의 홀딩 및 Snackbar 메시지를 표시하는 로직을 처리.
 */
@Stable
class MovieTrendsScaffoldState(
    val snackBarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)
                    snackbarManager.setMessageShown(message.id)
                    snackBarHostState.showSnackbar(text.toString())
                }
            }
        }
    }
}

/**
 * 현재 앱의 리소스를 반환하는 Composable 함수
 * Configuration 업데이트 시, LocalConfiguration.current가 변경되면서 다시 컴포즈된다.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
