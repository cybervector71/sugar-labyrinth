package com.Famdiba1.Huqsoz.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val resetDialog = MutableStateFlow(false)

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        progressRepository.progress,
        resetDialog
    ) { settings, progress, reset ->
        SettingsUiState(
            expertBoards = settings.expertBoards,
            flavorMarks = settings.flavorMarks,
            autoDryRun = settings.autoDryRun,
            confirmExit = settings.confirmExit,
            resetOpen = reset,
            stars = progress.totalStars,
            tokens = progress.tokens
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingsUiState()
    )

    fun toggleExpert() {
        val current = settingsRepository.settings.value
        settingsRepository.update(current.copy(expertBoards = !current.expertBoards))
    }

    fun toggleMarks() {
        val current = settingsRepository.settings.value
        settingsRepository.update(current.copy(flavorMarks = !current.flavorMarks))
    }

    fun toggleAutoDryRun() {
        val current = settingsRepository.settings.value
        settingsRepository.update(current.copy(autoDryRun = !current.autoDryRun))
    }

    fun toggleConfirmExit() {
        val current = settingsRepository.settings.value
        settingsRepository.update(current.copy(confirmExit = !current.confirmExit))
    }

    fun askReset() {
        resetDialog.value = true
    }

    fun dismissReset() {
        resetDialog.value = false
    }

    fun confirmReset() {
        progressRepository.reset()
        resetDialog.value = false
    }
}
