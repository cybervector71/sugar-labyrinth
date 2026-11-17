package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.core.theme.ClayShadow
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight

@Composable
fun ClayCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceLight.copy(alpha = 0.82f),
    borderColor: Color = Copper.copy(alpha = 0.45f),
    radius: Dp = 28.dp,
    elevation: Dp = 12.dp,
    borderWidth: Dp = 2.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = ClayShadow,
                spotColor = ClayShadow
            )
            .background(color = containerColor, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape),
        content = content
    )
}
