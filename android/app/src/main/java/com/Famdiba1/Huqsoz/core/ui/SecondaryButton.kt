package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.TextMuted
import com.Famdiba1.Huqsoz.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    accent: Color = Copper,
    labelColor: Color = TextPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 900f),
        label = "secondaryPress"
    )
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = RoundedCornerShape(18.dp),
        contentPadding = contentPadding,
        border = BorderStroke(
            width = 2.dp,
            color = if (enabled) accent.copy(alpha = 0.7f) else DisabledFill
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceLight.copy(alpha = 0.6f),
            contentColor = labelColor,
            disabledContainerColor = DisabledFill.copy(alpha = 0.4f),
            disabledContentColor = TextMuted
        ),
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
