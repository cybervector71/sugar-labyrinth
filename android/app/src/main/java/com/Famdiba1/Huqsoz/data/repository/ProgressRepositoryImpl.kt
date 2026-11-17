package com.Famdiba1.Huqsoz.data.repository

import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.data.local.PreferencesStore
import com.Famdiba1.Huqsoz.domain.model.Progress
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    private val state = MutableStateFlow(load())

    override val progress: StateFlow<Progress> = state.asStateFlow()

    override fun recordRound(chapter: Int, board: Int, stars: Int, score: Int, tokensEarned: Int) {
        val current = state.value
        val index = chapter * GameConfig.BOARDS_PER_CHAPTER + board
        val updatedStars = current.boardStars.toMutableList()
        if (index in updatedStars.indices && stars > updatedStars[index]) {
            updatedStars[index] = stars
        }
        val updated = current.copy(
            boardStars = updatedStars,
            bestScore = maxOf(current.bestScore, score),
            tokens = (current.tokens + tokensEarned).coerceIn(0, GameConfig.MAX_TOKENS)
        )
        persist(updated)
    }

    override fun spendToken() {
        val current = state.value
        if (current.tokens <= 0) return
        persist(current.copy(tokens = current.tokens - 1))
    }

    override fun reset() {
        persist(Progress())
    }

    private fun persist(progress: Progress) {
        store.writeString(KEY_STARS, progress.boardStars.joinToString(separator = ","))
        store.writeInt(KEY_BEST, progress.bestScore)
        store.writeInt(KEY_TOKENS, progress.tokens)
        state.value = progress
    }

    private fun load(): Progress {
        val raw = store.readString(KEY_STARS, "")
        val stars = if (raw.isBlank()) {
            List(GameConfig.TOTAL_BOARDS) { 0 }
        } else {
            val parsed = raw.split(",").mapNotNull { it.trim().toIntOrNull() }
            List(GameConfig.TOTAL_BOARDS) { parsed.getOrElse(it) { 0 } }
        }
        return Progress(
            boardStars = stars,
            bestScore = store.readInt(KEY_BEST, 0),
            tokens = store.readInt(KEY_TOKENS, 0)
        )
    }

    private companion object {
        const val KEY_STARS = "board_stars"
        const val KEY_BEST = "best_score"
        const val KEY_TOKENS = "copper_tokens"
    }
}
