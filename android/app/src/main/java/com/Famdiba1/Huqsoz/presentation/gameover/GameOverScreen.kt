package com.Famdiba1.Huqsoz.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DarkScrim
import com.Famdiba1.Huqsoz.core.theme.DarkScrimDeep
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.StarGold
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.FlavorBadge
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.core.ui.StarRow
import com.Famdiba1.Huqsoz.core.ui.StatCard
import com.Famdiba1.Huqsoz.core.ui.flavorLabel
import com.Famdiba1.Huqsoz.core.ui.groupedNumber
import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.model.RoundOutcome
import com.Famdiba1.Huqsoz.domain.model.RoundResult

@Composable
fun GameOverScreen(
    result: RoundResult,
    onPlayAgain: () -> Unit,
    onNextBoard: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    GameOverContent(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onNextBoard = onNextBoard,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    result: RoundResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onNextBoard: () -> Unit,
    onMenu: () -> Unit
) {
    val accent = if (result.won) Pistachio else Raspberry
    val cardScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 320f),
        label = "resultCard"
    )
    val title = when (result.outcome) {
        RoundOutcome.PLAN_FILLED -> R.string.outcome_plan_filled
        RoundOutcome.BATCH_SPOILED -> R.string.outcome_batch_spoiled
        RoundOutcome.LINE_BURST -> R.string.outcome_line_burst
        RoundOutcome.OUT_OF_MOVES -> R.string.outcome_out_of_moves
    }
    val cause = when (result.outcome) {
        RoundOutcome.PLAN_FILLED -> stringResource(R.string.cause_filled)
        RoundOutcome.BATCH_SPOILED -> stringResource(
            R.string.cause_spoiled,
            stringResource(flavorLabel(result.spoiledBy ?: result.required.keys.firstOrNull() ?: Flavor.RASPBERRY))
        )
        RoundOutcome.LINE_BURST -> stringResource(R.string.cause_burst)
        RoundOutcome.OUT_OF_MOVES -> stringResource(R.string.cause_moves, result.missingUnits)
    }

    ScreenBackground(
        overlay = listOf(
            DarkScrim.copy(alpha = 0.86f),
            accent.copy(alpha = 0.34f),
            DarkScrimDeep.copy(alpha = 0.90f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ClayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = cardScale
                        scaleY = cardScale
                    },
                containerColor = VanillaCream.copy(alpha = 0.96f),
                borderColor = Copper.copy(alpha = 0.6f),
                radius = 30.dp,
                elevation = 18.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = accent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = cause,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(14.dp))
                    StarRow(filled = result.stars, starSize = 30.dp, gap = 10.dp)
                    Spacer(modifier = Modifier.size(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (entry in result.required.entries) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                FlavorBadge(flavor = entry.key, size = 30.dp, showMark = true)
                                Text(
                                    text = stringResource(
                                        R.string.chip_progress,
                                        (result.delivered[entry.key] ?: 0).coerceAtMost(entry.value),
                                        entry.value
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                    val stats = ArrayList<StatEntry>()
                    if (result.score > 0) {
                        stats.add(
                            StatEntry(
                                value = groupedNumber(result.score),
                                label = stringResource(R.string.score_label),
                                color = Copper
                            )
                        )
                    }
                    if (result.movesLeft > 0) {
                        stats.add(
                            StatEntry(
                                value = result.movesLeft.toString(),
                                label = stringResource(R.string.moves_left_label),
                                color = Raspberry
                            )
                        )
                    }
                    if (state.bestScore > 0) {
                        stats.add(
                            StatEntry(
                                value = groupedNumber(state.bestScore),
                                label = stringResource(R.string.best_score),
                                color = StarGold
                            )
                        )
                    }
                    if (result.tokensEarned > 0) {
                        stats.add(
                            StatEntry(
                                value = stringResource(R.string.tokens_earned, result.tokensEarned),
                                label = stringResource(R.string.tokens_label),
                                color = Pistachio
                            )
                        )
                    }
                    if (stats.size >= 2) {
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            for (stat in stats.take(3)) {
                                StatCard(
                                    value = stat.value,
                                    label = stat.label,
                                    valueColor = stat.color,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth(),
                height = 60.dp
            )

            if (result.won && result.hasNextBoard) {
                Spacer(modifier = Modifier.size(12.dp))
                SecondaryButton(
                    text = stringResource(R.string.next_board),
                    onClick = onNextBoard,
                    modifier = Modifier.fillMaxWidth(),
                    accent = Pistachio
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth(),
                accent = Copper
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.stars_total, state.totalStars),
                    style = MaterialTheme.typography.labelSmall,
                    color = VanillaCream,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class StatEntry(
    val value: String,
    val label: String,
    val color: androidx.compose.ui.graphics.Color
)
