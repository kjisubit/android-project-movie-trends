package com.js.movietrends.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.js.movietrends.ui.theme.MovieTrendsTheme
import kotlin.math.ln

/**
 * [androidx.compose.material3.Surface]를 대체하고,
 * [com.js.movietrends.ui.theme.MovieTrendsColors]를 활용하도록 변경
 */
@Composable
fun MovieTrendsSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MovieTrendsTheme.colors.uiBackground,
    contentColor: Color = MovieTrendsTheme.colors.onBrand,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false
            )
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .background(
                color = getBackgroundColorForElevation(color, elevation),
                shape = shape
            )
            .clip(shape)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content
        )
    }
}

@Composable
private fun getBackgroundColorForElevation(color: Color, elevation: Dp): Color {
    return if (elevation > 0.dp) {
        color.withElevation(elevation)
    } else {
        color
    }
}

/**
 * elevation에 기반하여 white 오버레이 적용
 */
private fun Color.withElevation(elevation: Dp): Color {
    val foreground = calculateForeground(elevation)
    return foreground.compositeOver(this)
}

/**
 * 주어진 elevation을 사용해 오버레이 색상인 foreground 계산
 * 공식 문서에 있는 수식 활용
 */
private fun calculateForeground(elevation: Dp): Color {
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return Color.White.copy(alpha = alpha)
}
