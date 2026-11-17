package com.Famdiba1.Huqsoz.domain.repository

import com.Famdiba1.Huqsoz.domain.model.Progress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<Progress>

    fun recordRound(chapter: Int, board: Int, stars: Int, score: Int, tokensEarned: Int)

    fun spendToken()

    fun reset()
}
