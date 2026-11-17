package com.Famdiba1.Huqsoz.domain.usecase

import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.model.RoundOutcome

data class RoundScore(val stars: Int, val score: Int, val tokensEarned: Int)

class ScoreRoundUseCase {

    operator fun invoke(
        outcome: RoundOutcome,
        delivered: Map<Flavor, Int>,
        required: Map<Flavor, Int>,
        movesLeft: Int,
        movesBudget: Int,
        bonusHeld: Boolean
    ): RoundScore {
        val units = required.entries.sumOf { entry ->
            minOf(delivered[entry.key] ?: 0, entry.value)
        }
        val won = outcome == RoundOutcome.PLAN_FILLED
        val ratio = if (movesBudget <= 0) 0f else movesLeft.toFloat() / movesBudget.toFloat()
        val stars = when {
            !won -> 0
            ratio >= 0.4f -> 3
            ratio >= 0.2f -> 2
            else -> 1
        }
        val bonus = if (won && bonusHeld) 1 else 0
        val score = 100 * units + 25 * movesLeft.coerceAtLeast(0) + 150 * stars + 200 * bonus
        return RoundScore(stars = stars, score = score, tokensEarned = bonus)
    }
}
