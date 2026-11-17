package com.Famdiba1.Huqsoz.presentation.howtoplay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Caramel
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.TextMuted
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.FlavorBadge
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.core.ui.SectionTitle
import com.Famdiba1.Huqsoz.core.ui.flavorLabel
import com.Famdiba1.Huqsoz.domain.model.Flavor

private enum class RuleDiagram {
    ROTATE,
    PLAN,
    PRESSURE,
    CONTAMINATION
}

@Composable
fun HowToPlayScreen(onMenu: () -> Unit) {
    val howToPlayViewModel: HowToPlayViewModel = viewModel(factory = AppViewModelFactory)
    val state by howToPlayViewModel.state.collectAsStateWithLifecycle()
    HowToPlayContent(state = state, onMenu = onMenu)
}

@Composable
private fun HowToPlayContent(
    state: HowToPlayUiState,
    onMenu: () -> Unit
) {
    val scroll = rememberScrollState()
    ScreenBackground(
        overlay = listOf(
            VanillaCream.copy(alpha = 0.94f),
            DisabledFill.copy(alpha = 0.94f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    height = 48.dp
                )
                Text(
                    text = stringResource(R.string.howto_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RuleCard(
                    diagram = RuleDiagram.ROTATE,
                    title = stringResource(R.string.howto_rotate_title),
                    body = stringResource(R.string.howto_rotate_body),
                    accent = Copper
                )
                RuleCard(
                    diagram = RuleDiagram.PLAN,
                    title = stringResource(R.string.howto_plan_title),
                    body = stringResource(R.string.howto_plan_body),
                    accent = Pistachio
                )
                RuleCard(
                    diagram = RuleDiagram.PRESSURE,
                    title = stringResource(R.string.howto_pressure_title),
                    body = stringResource(R.string.howto_pressure_body),
                    accent = Caramel
                )
                RuleCard(
                    diagram = RuleDiagram.CONTAMINATION,
                    title = stringResource(R.string.howto_contamination_title),
                    body = stringResource(R.string.howto_contamination_body),
                    accent = Raspberry
                )

                SectionTitle(text = stringResource(R.string.howto_legend))

                ClayCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = SurfaceLight.copy(alpha = 0.86f),
                    borderColor = Copper.copy(alpha = 0.35f),
                    radius = 24.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (flavor in state.flavors) {
                            LegendItem(flavor = flavor, showMark = state.flavorMarks)
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.howto_disclaimer),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(flavor: Flavor, showMark: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FlavorBadge(flavor = flavor, size = 36.dp, showMark = showMark, describe = true)
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(flavorLabel(flavor)),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RuleCard(
    diagram: RuleDiagram,
    title: String,
    body: String,
    accent: Color
) {
    val painterAccent = remember(accent) { accent }
    ClayCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = SurfaceLight.copy(alpha = 0.88f),
        borderColor = accent.copy(alpha = 0.45f),
        radius = 24.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Canvas(modifier = Modifier.size(56.dp)) {
                drawRuleDiagram(diagram, painterAccent)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = accent
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

private fun DrawScope.drawRuleDiagram(diagram: RuleDiagram, accent: Color) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val unit = size.minDimension
    when (diagram) {
        RuleDiagram.ROTATE -> {
            drawArc(
                color = accent,
                startAngle = 40f,
                sweepAngle = 280f,
                useCenter = false,
                topLeft = Offset(unit * 0.16f, unit * 0.16f),
                size = Size(unit * 0.68f, unit * 0.68f),
                style = Stroke(width = unit * 0.1f, cap = StrokeCap.Round)
            )
            drawLine(
                color = Copper,
                start = Offset(center.x, center.y - unit * 0.1f),
                end = Offset(center.x, center.y + unit * 0.22f),
                strokeWidth = unit * 0.12f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Copper,
                start = center,
                end = Offset(center.x + unit * 0.22f, center.y),
                strokeWidth = unit * 0.12f,
                cap = StrokeCap.Round
            )
        }
        RuleDiagram.PLAN -> {
            for (index in 0 until 3) {
                drawRoundRect(
                    color = if (index < 2) accent else DisabledFill,
                    topLeft = Offset(unit * 0.14f, unit * (0.16f + index * 0.24f)),
                    size = Size(unit * 0.72f, unit * 0.16f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.08f, unit * 0.08f)
                )
            }
        }
        RuleDiagram.PRESSURE -> {
            drawArc(
                color = DisabledFill,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = Offset(unit * 0.12f, unit * 0.12f),
                size = Size(unit * 0.76f, unit * 0.76f),
                style = Stroke(width = unit * 0.12f, cap = StrokeCap.Round)
            )
            drawArc(
                color = accent,
                startAngle = 150f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(unit * 0.12f, unit * 0.12f),
                size = Size(unit * 0.76f, unit * 0.76f),
                style = Stroke(width = unit * 0.12f, cap = StrokeCap.Round)
            )
            drawLine(
                color = Copper,
                start = center,
                end = Offset(center.x + unit * 0.24f, center.y - unit * 0.18f),
                strokeWidth = unit * 0.08f,
                cap = StrokeCap.Round
            )
        }
        RuleDiagram.CONTAMINATION -> {
            drawRoundRect(
                color = Pistachio.copy(alpha = 0.4f),
                topLeft = Offset(unit * 0.16f, unit * 0.46f),
                size = Size(unit * 0.68f, unit * 0.4f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.1f, unit * 0.1f)
            )
            drawCircle(color = accent, radius = unit * 0.12f, center = Offset(center.x, unit * 0.26f))
            drawLine(
                color = accent,
                start = Offset(unit * 0.26f, unit * 0.38f),
                end = Offset(unit * 0.74f, unit * 0.86f),
                strokeWidth = unit * 0.08f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = accent,
                start = Offset(unit * 0.74f, unit * 0.38f),
                end = Offset(unit * 0.26f, unit * 0.86f),
                strokeWidth = unit * 0.08f,
                cap = StrokeCap.Round
            )
        }
    }
}
