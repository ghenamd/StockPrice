package com.stockprice.presentation.companyinfo.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockprice.domain.model.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    infos: List<IntraDayInfo>,
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    val upperClosePrice = remember {
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }

    val lowerClosePrice = remember {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }
    val textMarginBottom = 4

    /** Get device's density to calculate textSize in Pixels */
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        /** Hours at the bottom */
        val spacePerHour = (size.width - spacing) / infos.size
        (0 until infos.size - 1 step 2).forEach { index ->
            val info = infos[index]
            val hour = info.date.hour

            // Draw Text on native Canvas
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + index * spacePerHour,
                    size.height - textMarginBottom,
                    textPaint
                )
            }
        }

        /** Prices on the left */
        val priceStep = (upperClosePrice - lowerClosePrice) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerClosePrice + priceStep * i).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5,
                    textPaint
                )
            }
        }

        /** Draw path/graph line */
        var lastX = 0f
        var lastY = 0f
        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()

                val leftRatio = (info.close - lowerClosePrice) / (upperClosePrice - lowerClosePrice)
                val rightRatio =
                    (nextInfo.close - lowerClosePrice) / (upperClosePrice - lowerClosePrice)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()

                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()

                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) / 2f
                lastY = (y1 + y2) / 2f
                // In quadraticBezier x2 and y2 are control points that indicate how the curve is rounded.
                // Round/smooth curve by (x1 + x2) / 2 or (y1 + y2) / 2
                quadraticBezierTo(x1, y1, lastX, (y1 + y2) / 2f)
            }
        }

        /** Draw gradient below the path */
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

        drawPath(
            path = fillPath, brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

        drawPath(
            path = strokePath, color = graphColor, style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
