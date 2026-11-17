package com.Famdiba1.Huqsoz.presentation.menu

import com.Famdiba1.Huqsoz.core.config.GameConfig

data class MenuUiState(
    val bestScore: Int = 0,
    val stars: Int = 0,
    val totalStars: Int = GameConfig.TOTAL_STARS,
    val tokens: Int = 0,
    val resumeChapter: Int = 0,
    val resumeBoard: Int = 0,
    val expertBoards: Boolean = false
) {
    val hasProgress: Boolean get() = bestScore > 0 && stars > 0
}
