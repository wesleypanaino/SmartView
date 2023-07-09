package com.example.smartview.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.smartview.ui.utils.TextPainter

@Composable
fun ScatterPlot(data: List<Pair<Float, Float>>, modifier: Modifier = Modifier) {
    val paddingDp = 30.dp
    val pointRadius = 8.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.5f)
    ) {
        val textPainterY = remember { TextPainter() }
        val textPainterX = remember { TextPainter() }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pointRadiusPx = pointRadius.toPx()
            val padding = paddingDp.toPx()
            val dataMinX = data.minOf { it.first }
            val dataMaxX = data.maxOf { it.first }
            val dataMinY = data.minOf { it.second }
            val dataMaxY = data.maxOf { it.second }

            val points = data.map { pair ->
                Offset(
                    x = padding + (pair.first - dataMinX) / (dataMaxX - dataMinX) * (size.width - 2 * padding),
                    y = padding + (dataMaxY - pair.second) / (dataMaxY - dataMinY) * (size.height - 2 * padding)
                )
            }

            // Draw border
            drawRect(
                color = Color.LightGray,
                topLeft = Offset(padding, padding),
                size = Size(size.width - 2 * padding, size.height - 2 * padding),
                style = Stroke(width = 1.dp.toPx())
            )

            // Draw grid lines and labels
            val gridLines = 5
            for (i in 0..gridLines) {
                val yPos = padding + i * (size.height - 2 * padding) / gridLines
                val xPos = padding + i * (size.width - 2 * padding) / gridLines
                // Horizontal grid line
                drawLine(
                    color = Color.LightGray,
                    start = Offset(padding, yPos),
                    end = Offset(size.width - padding, yPos)
                )
                // Vertical grid line
                drawLine(
                    color = Color.LightGray,
                    start = Offset(xPos, padding),
                    end = Offset(xPos, size.height - padding)
                )
                // Y-axis label
                textPainterY.text =
                    ((dataMinY + (gridLines - i).toFloat() / gridLines * (dataMaxY - dataMinY))).toInt()
                        .toString()
                textPainterY.color = Color.Black
                textPainterY.paint(drawContext.canvas, Offset(padding / 2, yPos))

                // X-axis label
                textPainterX.text = (i.toFloat() / gridLines * data.size).toInt().toString()
                textPainterX.color = Color.Black
                textPainterX.paint(drawContext.canvas, Offset(xPos, size.height - padding / 2))
            }

            points.forEach { point ->
                drawCircle(
                    color = Color.Blue,
                    center = point,
                    radius = pointRadiusPx
                )
            }
        }
    }
}