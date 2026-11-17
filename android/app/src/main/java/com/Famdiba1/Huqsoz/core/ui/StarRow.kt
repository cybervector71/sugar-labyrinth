package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.StarGold

@Composable
fun StarRow(
    filled: Int,
    modifier: Modifier = Modifier,
    total: Int = 3,
    starSize: Dp = 16.dp,
    gap: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            val color = if (index < filled) StarGold else DisabledFill
            Canvas(modifier = Modifier.size(starSize)) {
                drawStar(color)
            }
        }
    }
}

private fun DrawScope.drawStar(color: Color) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val outer = size.minDimension / 2f
    val inner = outer * 0.46f
    val path = Path()
    for (index in 0 until 10) {
        val radius = if (index % 2 == 0) outer else inner
        val angle = Math.toRadians((-90f + index * 36f).toDouble())
        val x = center.x + radius * kotlin.math.cos(angle).toFloat()
        val y = center.y + radius * kotlin.math.sin(angle).toFloat()
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path = path, color = color)
}
