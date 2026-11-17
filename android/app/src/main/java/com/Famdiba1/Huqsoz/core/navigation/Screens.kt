package com.Famdiba1.Huqsoz.core.navigation

import com.Famdiba1.Huqsoz.domain.model.RoundResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelSelect : Screen
    data object HowToPlay : Screen
    data object Settings : Screen
    data class Game(val chapter: Int, val board: Int, val expert: Boolean, val session: Int) : Screen
    data class GameOver(val result: RoundResult) : Screen
}
