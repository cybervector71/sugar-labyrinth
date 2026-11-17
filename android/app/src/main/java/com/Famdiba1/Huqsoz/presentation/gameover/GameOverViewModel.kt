package com.Famdiba1.Huqsoz.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GameOverUiState(
    val bestScore: Int = 0,
    val totalStars: Int = 0,
    val tokens: Int = 0
)

class GameOverViewModel(progressRepository: ProgressRepository) : ViewModel() {

    val state: StateFlow<GameOverUiState> = progressRepository.progress
        .map { progress ->
            GameOverUiState(
                bestScore = progress.bestScore,
                totalStars = progress.totalStars,
                tokens = progress.tokens
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = GameOverUiState()
        )
}
