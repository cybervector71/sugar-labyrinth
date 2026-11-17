package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.domain.model.Flavor

@Composable
fun PlanChip(
    flavor: Flavor,
    delivered: Int,
    required: Int,
    modifier: Modifier = Modifier,
    showMark: Boolean = true
) {
    if (required <= 0) return
    val satisfied = delivered >= required
    val accent = flavorColor(flavor)
    val border = if (satisfied) Pistachio else accent.copy(alpha = 0.6f)
    val label = stringResource(R.string.chip_progress, delivered.coerceAtMost(required), required)
    val flavorName = stringResource(flavorLabel(flavor))
    val description = stringResource(R.string.chip_state, flavorName, delivered.coerceAtMost(required), required)
    Row(
        modifier = modifier
            .background(
                color = if (satisfied) Pistachio.copy(alpha = 0.22f) else SurfaceLight.copy(alpha = 0.74f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(width = 2.dp, color = border, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .semantics { stateDescription = description },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FlavorBadge(flavor = flavor, size = 22.dp, showMark = showMark)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
        if (satisfied) {
            Text(
                text = stringResource(R.string.tick),
                style = MaterialTheme.typography.bodyMedium,
                color = Pistachio
            )
        }
    }
}
