package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.theme.Caramel
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DarkScrim
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.VanillaCream

@Composable
fun PressureGauge(
    value: Int,
    max: Int,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val fraction = if (max <= 0) 0f else (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "pressure"
    )
    val arcColor = when {
        animated < 0.45f -> Pistachio
        animated < 0.75f -> Caramel
        else -> Raspberry
    }
    val description = stringResource(R.string.pressure_state, value, max)
    Box(
        modifier = modifier
            .size(size)
            .semantics { stateDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = this.size.minDimension * 0.14f
            val inset = stroke / 2f
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            drawCircle(color = VanillaCream, radius = this.size.minDimension / 2f - stroke * 0.2f)
            drawArc(
                color = DisabledFill,
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = arcColor,
                startAngle = 140f,
                sweepAngle = 260f * animated,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            val angle = Math.toRadians((140f + 260f * animated).toDouble())
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val needle = this.size.minDimension / 2f - stroke * 1.6f
            drawLine(
                color = DarkScrim,
                start = center,
                end = Offset(
                    center.x + needle * kotlin.math.cos(angle).toFloat(),
                    center.y + needle * kotlin.math.sin(angle).toFloat()
                ),
                strokeWidth = stroke * 0.42f,
                cap = StrokeCap.Round
            )
            drawCircle(color = Copper, radius = stroke * 0.45f, center = center)
        }
    }
}
