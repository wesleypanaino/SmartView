package com.example.smartview.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas

// TextPainter that can draw text on a native Canvas.
class TextPainter(
    var text: String = "",
    var color: Color = Color.Black,
    var fontSize: Float = 14f
) {
    fun paint(canvas: Canvas, position: Offset) {
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = android.graphics.Color.BLACK
            textSize = fontSize
        }
        canvas.nativeCanvas.drawText(text, position.x, position.y, paint)
    }
}