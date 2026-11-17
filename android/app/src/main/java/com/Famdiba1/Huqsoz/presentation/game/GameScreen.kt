package com.Famdiba1.Huqsoz.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.core.di.GameViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.FactoryFloor
import com.Famdiba1.Huqsoz.core.theme.FactoryFloorMid
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.SurfaceAlt
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.PlanChip
import com.Famdiba1.Huqsoz.core.ui.PressureGauge
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.domain.model.RoundResult
import com.Famdiba1.Huqsoz.presentation.dialog.ConfirmDialog
import com.Famdiba1.Huqsoz.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    chapter: Int,
    board: Int,
    expert: Boolean,
    session: Int,
    onGameOver: (RoundResult) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(chapter, board, expert),
        key = "game-$chapter-$board-$expert-$session"
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onGameOver(event.result)
                GameEvent.ExitToMenu -> onMenu()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onTileTap = gameViewModel::onTileTap,
        onPour = gameViewModel::pour,
        onDryRun = gameViewModel::dryRun,
        onVent = gameViewModel::vent,
        onToken = gameViewModel::armToken,
        onExit = gameViewModel::requestExit,
        onPause = gameViewModel::openPause,
        onResume = gameViewModel::closePause,
        onRestart = gameViewModel::restart,
        onLeave = gameViewModel::leave,
        onDismissExit = gameViewModel::dismissExit
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onTileTap: (Int) -> Unit,
    onPour: () -> Unit,
    onDryRun: () -> Unit,
    onVent: () -> Unit,
    onToken: () -> Unit,
    onExit: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onLeave: () -> Unit,
    onDismissExit: () -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            SurfaceAlt.copy(alpha = 0.88f),
            FactoryFloorMid.copy(alpha = 0.90f),
            FactoryFloor.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                state = state,
                onExit = onExit,
                onPause = onPause
            )

            PlanBanner(
                state = state,
                onToken = onToken,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val frame = (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp
                    val widthLimit = minOf(maxWidth - 32.dp, GameConfig.BOARD_MAX_W_DP.dp)
                    val heightLimit = maxHeight - 150.dp
                    val tileByWidth = (widthLimit - frame * 2) / GameConfig.GRID_COLS
                    val tileByHeight = (heightLimit - frame * 2) / GameConfig.GRID_ROWS
                    val tile = minOf(tileByWidth, tileByHeight, 58.dp)
                    val boardWidth = tile * GameConfig.GRID_COLS + frame * 2
                    val boardHeight = tile * GameConfig.GRID_ROWS + frame * 2
                    ClayCard(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .width(boardWidth)
                            .height(boardHeight),
                        containerColor = VanillaCream.copy(alpha = 0.9f),
                        borderColor = Copper.copy(alpha = 0.5f),
                        radius = 24.dp,
                        elevation = 16.dp
                    ) {
                        BoardGrid(
                            state = state,
                            tileSize = tile,
                            onTap = onTileTap,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(GameConfig.BOARD_PAD_DP.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.hintVisible) {
                        Text(
                            text = stringResource(R.string.game_hint),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(
                                    color = SurfaceLight.copy(alpha = 0.78f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    ControlBar(
                        state = state,
                        onPour = onPour,
                        onDryRun = onDryRun,
                        onVent = onVent
                    )
                }
            }
        }

        if (state.pauseOpen) {
            PauseDialog(
                onResume = onResume,
                onRestart = {
                    onResume()
                    onRestart()
                },
                onMenu = onLeave
            )
        }

        if (state.exitOpen) {
            ConfirmDialog(
                title = stringResource(R.string.confirm_exit_title),
                body = stringResource(R.string.confirm_exit_body),
                confirmLabel = stringResource(R.string.menu),
                dismissLabel = stringResource(R.string.cancel),
                onConfirm = onLeave,
                onDismiss = onDismissExit
            )
        }
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onExit: () -> Unit,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                color = SurfaceLight.copy(alpha = 0.72f),
                shape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderIcon(
            iconRes = R.drawable.icon_home,
            description = stringResource(R.string.cd_menu),
            onClick = onExit
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    R.string.board_label,
                    state.chapter + 1,
                    state.boardIndex + 1
                ),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = stringResource(R.string.moves_value, state.moves.coerceAtLeast(0)),
                style = MaterialTheme.typography.titleMedium,
                color = Raspberry,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
        HeaderIcon(
            iconRes = R.drawable.icon_pause,
            description = stringResource(R.string.cd_pause),
            onClick = onPause
        )
    }
}

@Composable
private fun HeaderIcon(
    iconRes: Int,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = VanillaCream.copy(alpha = 0.92f), shape = CircleShape)
            .border(width = 2.dp, color = Copper.copy(alpha = 0.45f), shape = CircleShape)
            .clickable(enabled = true, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun PlanBanner(
    state: GameUiState,
    onToken: () -> Unit,
    modifier: Modifier = Modifier
) {
    val description = stringResource(R.string.plan_state, state.planDone, state.planTotal)
    ClayCard(
        modifier = modifier.height(64.dp),
        containerColor = SurfaceLight.copy(alpha = 0.8f),
        borderColor = Copper.copy(alpha = 0.4f),
        radius = 22.dp,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .semantics { stateDescription = description },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (entry in state.plan.entries) {
                PlanChip(
                    flavor = entry.key,
                    delivered = state.delivered[entry.key] ?: 0,
                    required = entry.value,
                    showMark = state.flavorMarks
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.tokens > 0) {
                SecondaryButton(
                    text = stringResource(R.string.token_chip, state.tokens),
                    onClick = onToken,
                    height = 44.dp,
                    accent = if (state.tokenArmed) Pistachio else Copper
                )
            }
        }
    }
}

@Composable
private fun ControlBar(
    state: GameUiState,
    onPour: () -> Unit,
    onDryRun: () -> Unit,
    onVent: () -> Unit
) {
    ClayCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        containerColor = SurfaceLight.copy(alpha = 0.86f),
        borderColor = Copper.copy(alpha = 0.42f),
        radius = 28.dp,
        elevation = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PressureGauge(
                value = state.pressure,
                max = state.pressureMax,
                size = 52.dp
            )
            PrimaryButton(
                text = stringResource(R.string.go),
                onClick = onPour,
                modifier = Modifier.weight(1.1f),
                enabled = !state.busy,
                height = 56.dp
            )
            SecondaryButton(
                text = stringResource(R.string.dry_run, state.dryRunsLeft),
                onClick = onDryRun,
                modifier = Modifier.weight(1f),
                enabled = state.dryRunsLeft > 0 && !state.busy,
                height = 52.dp,
                accent = Pistachio
            )
            SecondaryButton(
                text = stringResource(R.string.vent),
                onClick = onVent,
                modifier = Modifier.weight(0.9f),
                enabled = state.pressure >= 3 && state.moves >= GameConfig.VENT_MOVE_COST && !state.busy,
                height = 52.dp,
                accent = Copper
            )
        }
    }
}

