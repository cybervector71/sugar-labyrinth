package com.Famdiba1.Huqsoz.presentation.levelselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.SurfaceSoft
import com.Famdiba1.Huqsoz.core.theme.TextMuted
import com.Famdiba1.Huqsoz.core.theme.TextPrimary
import com.Famdiba1.Huqsoz.core.theme.TextSecondary
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ClayCard
import com.Famdiba1.Huqsoz.core.ui.PrimaryButton
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground
import com.Famdiba1.Huqsoz.core.ui.SecondaryButton
import com.Famdiba1.Huqsoz.core.ui.StarRow

@Composable
fun LevelSelectScreen(
    onPlayBoard: (Int, Int, Boolean) -> Unit,
    onMenu: () -> Unit
) {
    val levelViewModel: LevelSelectViewModel = viewModel(factory = AppViewModelFactory)
    val state by levelViewModel.state.collectAsStateWithLifecycle()
    LevelSelectContent(
        state = state,
        onSelectChapter = levelViewModel::selectChapter,
        onPlayBoard = onPlayBoard,
        onMenu = onMenu
    )
}

@Composable
private fun LevelSelectContent(
    state: LevelSelectUiState,
    onSelectChapter: (Int) -> Unit,
    onPlayBoard: (Int, Int, Boolean) -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            SurfaceSoft.copy(alpha = 0.92f),
            VanillaCream.copy(alpha = 0.93f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = stringResource(R.string.chapters_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (tab in state.tabs) {
                    ChapterChip(
                        tab = tab,
                        selected = tab.index == state.selected,
                        modifier = Modifier.weight(1f),
                        onSelect = { onSelectChapter(tab.index) }
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            if (state.boards.firstOrNull()?.unlocked == true) {
                for (row in state.boards.chunked(3)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (cell in row) {
                            BoardTile(
                                cell = cell,
                                modifier = Modifier.weight(1f),
                                onPlay = { onPlayBoard(cell.chapter, cell.board, state.expertBoards) }
                            )
                        }
                    }
                }
            } else {
                ClayCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    containerColor = SurfaceLight.copy(alpha = 0.84f),
                    borderColor = Copper.copy(alpha = 0.32f)
                ) {
                    Text(
                        text = stringResource(R.string.chapter_locked),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (state.expertUnlocked) {
                ClayCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp),
                    containerColor = SurfaceLight.copy(alpha = 0.86f),
                    borderColor = Pistachio.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.expert_board),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        PrimaryButton(
                            text = stringResource(R.string.play),
                            onClick = { onPlayBoard(state.selected, 0, true) },
                            modifier = Modifier.fillMaxWidth(),
                            height = 50.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            } else {
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun ChapterChip(
    tab: ChapterTab,
    selected: Boolean,
    modifier: Modifier,
    onSelect: () -> Unit
) {
    val title = when (tab.index) {
        0 -> R.string.chapter_1
        1 -> R.string.chapter_2
        2 -> R.string.chapter_3
        else -> R.string.chapter_4
    }
    val container = when {
        selected -> Raspberry
        tab.unlocked -> SurfaceLight.copy(alpha = 0.78f)
        else -> DisabledFill
    }
    val label = when {
        selected -> VanillaCream
        tab.unlocked -> TextPrimary
        else -> TextMuted
    }
    Column(
        modifier = modifier
            .height(74.dp)
            .background(color = container, shape = RoundedCornerShape(18.dp))
            .border(
                width = 2.dp,
                color = if (selected) Copper else Copper.copy(alpha = 0.28f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(enabled = true, onClick = onSelect)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.labelSmall,
            color = label,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = if (tab.unlocked) {
                stringResource(R.string.chip_progress, tab.stars, 18)
            } else {
                stringResource(R.string.stars_to_unlock)
            },
            style = MaterialTheme.typography.labelSmall,
            color = label,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoardTile(
    cell: BoardCell,
    modifier: Modifier,
    onPlay: () -> Unit
) {
    ClayCard(
        modifier = modifier
            .aspectRatio(0.92f)
            .clickable(enabled = cell.unlocked, onClick = onPlay),
        containerColor = SurfaceLight.copy(alpha = if (cell.unlocked) 0.88f else 0.4f),
        borderColor = if (cell.stars > 0) Pistachio.copy(alpha = 0.6f) else Copper.copy(alpha = 0.35f),
        radius = 24.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (cell.unlocked) VanillaCream else DisabledFill,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Copper.copy(alpha = if (cell.unlocked) 0.6f else 0.25f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.board_number, cell.board + 1),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (cell.unlocked) TextPrimary else TextMuted
                )
            }
            Spacer(modifier = Modifier.size(6.dp))
            StarRow(filled = cell.stars, starSize = 13.dp)
        }
    }
}
