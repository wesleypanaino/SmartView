package com.example.smartview.ui.utils

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun DrawGraph(
    modifier: Modifier = Modifier,
    padding: Float,
    gridLines: Int,
    dataMin: Float,
    dataMax: Float,
    textPainterY: TextPainter,
    textPainterX: TextPainter,
    drawPoints: DrawScope.() -> Unit
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        for (i in 0 until gridLines) {
            val yPos = padding + i * (height - 2 * padding) / gridLines
            val xPos = padding + i * (width - 2 * padding) / gridLines
            drawLine(
                color = Color.LightGray,
                start = Offset(padding, yPos),
                end = Offset(width - padding, yPos)
            )
            drawLine(
                color = Color.LightGray,
                start = Offset(xPos, padding),
                end = Offset(xPos, height - padding)
            )
            textPainterY.text = ((dataMin + (gridLines - i).toFloat() / gridLines * (dataMax - dataMin))).toInt().toString()
            textPainterY.paint(drawContext.canvas, Offset(padding / 2, yPos))
            textPainterX.text = (dataMin + i.toFloat() / gridLines * (dataMax - dataMin)).toInt().toString()
            textPainterX.paint(drawContext.canvas, Offset(xPos, height - padding / 2))
        }
        drawPoints()
    }
}