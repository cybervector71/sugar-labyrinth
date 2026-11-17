package com.Famdiba1.Huqsoz.presentation.game

import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.model.RoundResult
import com.Famdiba1.Huqsoz.domain.usecase.FlowCell

enum class GamePhase {
    IDLE,
    DRY_RUN,
    POURING,
    FINISHED
}

data class GameUiState(
    val board: Board = Board.Empty,
    val plan: Map<Flavor, Int> = emptyMap(),
    val delivered: Map<Flavor, Int> = emptyMap(),
    val phase: GamePhase = GamePhase.IDLE,
    val chapter: Int = 0,
    val boardIndex: Int = 0,
    val expert: Boolean = false,
    val moves: Int = GameConfig.MOVES_BUDGET,
    val movesBudget: Int = GameConfig.MOVES_BUDGET,
    val pressure: Int = 0,
    val pressureMax: Int = GameConfig.PRESSURE_MAX,
    val dryRunsLeft: Int = GameConfig.DRY_RUNS_PER_BOARD,
    val tokens: Int = 0,
    val tokenArmed: Boolean = false,
    val flow: Map<Int, FlowCell> = emptyMap(),
    val fillDistance: Int = 0,
    val ghost: Boolean = false,
    val bonusHeld: Boolean = true,
    val hintVisible: Boolean = true,
    val shakeCell: Int = -1,
    val flavorMarks: Boolean = true,
    val pauseOpen: Boolean = false,
    val exitOpen: Boolean = false,
    val seed: Long = 0L
) {
    val planTotal: Int get() = plan.values.sum()

    val planDone: Int
        get() = plan.entries.sumOf { entry -> minOf(delivered[entry.key] ?: 0, entry.value) }

    val planComplete: Boolean get() = planTotal > 0 && planDone >= planTotal

    val busy: Boolean get() = phase != GamePhase.IDLE
}

sealed interface GameEvent {
    data class Finished(val result: RoundResult) : GameEvent
    data object ExitToMenu : GameEvent
}
