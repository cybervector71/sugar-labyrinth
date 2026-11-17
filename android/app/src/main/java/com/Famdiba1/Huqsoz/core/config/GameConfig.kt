package com.Famdiba1.Huqsoz.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val GRID_COLS = 6
    const val GRID_ROWS = 7
    const val MOVES_BUDGET = 18
    const val MOVES_BUDGET_EXPERT = 12
    const val PRESSURE_MAX = 10
    const val PRESSURE_MAX_EXPERT = 8
    const val POUR_STEP_MS = 120L
    const val DRY_RUN_MS = 1500L
    const val HINT_MS = 6000L
    const val SHAKE_MS = 140L
    const val BOARD_MAX_W_DP = 380
    const val BOARD_PAD_DP = 6
    const val BOARD_BORDER_DP = 2
    const val CHAPTER_STAR_GATE = 12
    const val CHAPTERS = 4
    const val BOARDS_PER_CHAPTER = 6
    const val TOTAL_BOARDS = CHAPTERS * BOARDS_PER_CHAPTER
    const val TOTAL_STARS = TOTAL_BOARDS * 3
    const val DRY_RUNS_PER_BOARD = 2
    const val VENT_RELIEF = 5
    const val VENT_MOVE_COST = 2
    const val WASTED_POUR_PRESSURE = 3
    const val MAX_TOKENS = 9
    const val GENERATE_ATTEMPTS = 20
    const val ROTATE_COST = 1
    const val DRY_PRESSURE = 1
    const val WET_PRESSURE = 2
}
