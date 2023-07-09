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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartview.ui.utils.TextPainter
import com.example.smartview.utils.JsonUtils

@Composable
fun LineChart(data: List<Float>, modifier: Modifier = Modifier) {
    val paddingDp = 30.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.5f)
    ) {
        val textPainterY = remember { TextPainter() }
        val textPainterX = remember { TextPainter() }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = paddingDp.toPx()
            val dataMin = data.minOrNull() ?: 0f
            val dataMax = data.maxOrNull() ?: 0f
            val points = data.mapIndexed { index, fl ->
                Offset(
                    x = padding + index.toFloat() / (data.size - 1) * (size.width - 2 * padding),
                    y = padding + (dataMax - fl) / (dataMax - dataMin) * (size.height - 2 * padding)
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
                    ((dataMin + (gridLines - i).toFloat() / gridLines * (dataMax - dataMin))).toInt()
                        .toString()
                textPainterY.color = Color.Black
                textPainterY.paint(drawContext.canvas, Offset(padding / 2, yPos))

                // X-axis label
                textPainterX.text = (i.toFloat() / gridLines * data.size).toInt().toString()
                textPainterX.color = Color.Black
                textPainterX.paint(drawContext.canvas, Offset(xPos, size.height - padding / 2))
            }

            // Draw chart line
            val linePath = Path().apply {
                points.forEachIndexed { index, point ->
                    if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
                }
            }
            drawPath(
                path = linePath,
                color = Color.Blue,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDemoGraph() {
    DemoGraph()
}

@Composable
fun DemoGraph() {
    val template = "{\n" +
        "  \"type\": \"single\",\n" +
        "  \"dataTemplate\": {\n" +
        "    \"elements\": [\n" +
        "      {\n" +
        "        \"type\": \"lineChart\",\n" +
        "        \"mapTo\": \"stockPrices\"\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}"
    val data = "[{\n" +
        "\"stockPrices\": [42.5, 45.3, 44.8, 46.1, 45.9, 47.0, 46.8, 47.5]\n" +
        "}]"

    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}