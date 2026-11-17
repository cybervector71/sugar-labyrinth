package com.Famdiba1.Huqsoz.domain.model

enum class RoundOutcome {
    PLAN_FILLED,
    BATCH_SPOILED,
    LINE_BURST,
    OUT_OF_MOVES
}

data class RoundResult(
    val outcome: RoundOutcome,
    val chapter: Int,
    val board: Int,
    val expert: Boolean,
    val delivered: Map<Flavor, Int>,
    val required: Map<Flavor, Int>,
    val movesLeft: Int,
    val stars: Int,
    val score: Int,
    val tokensEarned: Int,
    val bonusHeld: Boolean,
    val spoiledBy: Flavor?,
    val hasNextBoard: Boolean
) {
    val won: Boolean get() = outcome == RoundOutcome.PLAN_FILLED

    val missingUnits: Int
        get() = required.entries.sumOf { entry ->
            val done = delivered[entry.key] ?: 0
            if (done >= entry.value) 0 else entry.value - done
        }
}
