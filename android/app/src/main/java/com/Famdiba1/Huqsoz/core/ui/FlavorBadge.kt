package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.domain.model.Flavor

@Composable
fun FlavorBadge(
    flavor: Flavor,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    showMark: Boolean = true,
    describe: Boolean = false
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(flavorDrawable(flavor)),
            contentDescription = if (describe) stringResource(flavorLabel(flavor)) else null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size)
        )
        if (showMark) {
            val markSize = size * 0.38f
            Canvas(
                modifier = Modifier
                    .size(markSize)
                    .border(width = 1.dp, color = SurfaceLight.copy(alpha = 0.8f), shape = CircleShape)
            ) {
                drawFlavorMark(flavorMark(flavor), flavorColor(flavor))
            }
        }
    }
}

fun DrawScope.drawFlavorMark(mark: FlavorMark, color: Color) {
    val side = minOf(size.width, size.height)
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = side / 2f * 0.86f
    when (mark) {
        FlavorMark.CIRCLE -> drawCircle(color = color, radius = radius, center = center)
        FlavorMark.SQUARE -> drawRect(
            color = color,
            topLeft = Offset(center.x - radius * 0.8f, center.y - radius * 0.8f),
            size = Size(radius * 1.6f, radius * 1.6f)
        )
        FlavorMark.TRIANGLE -> drawPolygon(center, radius, 3, color, -90f)
        FlavorMark.DIAMOND -> drawPolygon(center, radius, 4, color, -90f)
        FlavorMark.HEXAGON -> drawPolygon(center, radius, 6, color, -90f)
    }
}

private fun DrawScope.drawPolygon(
    center: Offset,
    radius: Float,
    sides: Int,
    color: Color,
    startAngle: Float
) {
    val path = Path()
    for (index in 0 until sides) {
        val angle = Math.toRadians((startAngle + index * 360f / sides).toDouble())
        val x = center.x + radius * kotlin.math.cos(angle).toFloat()
        val y = center.y + radius * kotlin.math.sin(angle).toFloat()
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path = path, color = color)
}
