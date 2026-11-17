package com.Famdiba1.Huqsoz.presentation.levelselect

data class BoardCell(
    val chapter: Int,
    val board: Int,
    val stars: Int,
    val unlocked: Boolean
)

data class ChapterTab(
    val index: Int,
    val stars: Int,
    val unlocked: Boolean
)

data class LevelSelectUiState(
    val selected: Int = 0,
    val tabs: List<ChapterTab> = emptyList(),
    val boards: List<BoardCell> = emptyList(),
    val expertUnlocked: Boolean = false,
    val expertBoards: Boolean = false
)
