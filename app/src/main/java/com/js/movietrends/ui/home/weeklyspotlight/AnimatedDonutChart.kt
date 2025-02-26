package com.js.movietrends.ui.home.weeklyspotlight

import android.text.TextPaint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.js.movietrends.ui.utils.FormatUtil

@Composable
fun AnimatedDonutChart(
    value: Float,
    maxValue: Int,
    modifier: Modifier = Modifier,
    durationMillis: Int = 3000
) {
    var valueState by remember { mutableFloatStateOf(0.0f) }

    LaunchedEffect(value) {
        valueState = value
    }

    val targetProgressState by animateFloatAsState(
        targetValue = valueState,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = LinearEasing
        ),
        label = "DonutChartAnimation"
    )

    DonutChart(
        value = targetProgressState,
        maxValue = maxValue,
        modifier = modifier,
    )
}

@Composable
fun DonutChart(
    value: Float,
    maxValue: Int,
    modifier: Modifier = Modifier,
    chartColor: Color = Color.Green
) {
    Canvas(modifier = modifier) {
        val size = size.minDimension
        val strokeWidth = size * 0.05f
        val radius = size / 2
        val backgroundAlpha = 0.5f
        val contentAlpha = 0.5f

        // 백그라운드
        drawCircle(
            color = Color.Black.copy(alpha = backgroundAlpha),
            radius = radius - strokeWidth,
        )

        // 백그라운드 트랙
        drawCircle(
            color = Color.LightGray.copy(alpha = backgroundAlpha),
            radius = radius - strokeWidth / 2,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Butt
            )
        )

        // 트랙
        drawArc(
            color = chartColor.copy(alpha = contentAlpha),
            startAngle = -90f,
            sweepAngle = value / maxValue * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            size = Size(size - strokeWidth, size - strokeWidth),
            topLeft = Offset(
                strokeWidth / 2,
                strokeWidth / 2
            )
        )

        // 텍스트
        val rateString = "★ ${FormatUtil.formatToOneDecimal(value)}/$maxValue"
        val textPaint = TextPaint().apply {
            color = Color.White.toArgb()
            textSize = 30.sp.toPx()
        }
        val textWidth = textPaint.measureText(rateString)
        val textHeight = textPaint.descent() - textPaint.ascent()
        val textX = (size - textWidth) / 2
        val textY = (size + textHeight / 2) / 2
        drawContext.canvas.nativeCanvas.drawText(rateString, textX, textY, textPaint)
    }
}