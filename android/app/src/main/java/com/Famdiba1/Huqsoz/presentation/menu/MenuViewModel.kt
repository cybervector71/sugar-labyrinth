package com.Famdiba1.Huqsoz.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val state: StateFlow<MenuUiState> =
        combine(progressRepository.progress, settingsRepository.settings) { progress, settings ->
            val resume = progress.nextBoard()
            MenuUiState(
                bestScore = progress.bestScore,
                stars = progress.totalStars,
                tokens = progress.tokens,
                resumeChapter = resume.first,
                resumeBoard = resume.second,
                expertBoards = settings.expertBoards
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MenuUiState()
        )
}
