package com.Famdiba1.Huqsoz.presentation.settings

data class SettingsUiState(
    val expertBoards: Boolean = false,
    val flavorMarks: Boolean = true,
    val autoDryRun: Boolean = false,
    val confirmExit: Boolean = true,
    val resetOpen: Boolean = false,
    val stars: Int = 0,
    val tokens: Int = 0
)
