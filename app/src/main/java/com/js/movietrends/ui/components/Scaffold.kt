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
import com.js.movietrends.model.SnackBarManager
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
    snackBarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable (() -> Unit) = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    backgroundColor: Color = MovieTrendsTheme.colors.uiBackground,
    contentColor: Color = MovieTrendsTheme.colors.onBrand,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = {
            snackBarHost(snackBarHostState)
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = backgroundColor,
        contentColor = contentColor,
        content = content
    )
}

/**
 * MovieTrendsScaffoldState 인스턴스를 생성하고 기억
 */
@Composable
fun rememberMovieTrendsScaffoldState(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackBarManager: SnackBarManager = SnackBarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MovieTrendsScaffoldState =
    remember(snackBarHostState, snackBarManager, resources, coroutineScope) {
        MovieTrendsScaffoldState(snackBarHostState, snackBarManager, resources, coroutineScope)
    }

/**
 * ScaffoldState 홀딩 및 Snackbar 메시지 표시 로직 처리.
 */
@Stable
class MovieTrendsScaffoldState(
    private val snackBarHostState: SnackbarHostState,
    private val snackBarManager: SnackBarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackBarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)
                    snackBarManager.setMessageShown(message.id)
                    snackBarHostState.showSnackbar(text.toString())
                }
            }
        }
    }
}

/**
 * 현재 앱의 리소스 반환하는 Composable 함수
 * Configuration 업데이트 시, LocalConfiguration.current가 변경되면서 다시 컴포즈
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
