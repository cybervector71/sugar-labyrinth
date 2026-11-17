package com.Famdiba1.Huqsoz.data.repository

import com.Famdiba1.Huqsoz.data.local.PreferencesStore
import com.Famdiba1.Huqsoz.domain.model.GameSettings
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    private val state = MutableStateFlow(load())

    override val settings: StateFlow<GameSettings> = state.asStateFlow()

    override fun update(settings: GameSettings) {
        store.writeBoolean(KEY_EXPERT, settings.expertBoards)
        store.writeBoolean(KEY_MARKS, settings.flavorMarks)
        store.writeBoolean(KEY_AUTO_DRY, settings.autoDryRun)
        store.writeBoolean(KEY_CONFIRM, settings.confirmExit)
        state.value = settings
    }

    private fun load(): GameSettings = GameSettings(
        expertBoards = store.readBoolean(KEY_EXPERT, false),
        flavorMarks = store.readBoolean(KEY_MARKS, true),
        autoDryRun = store.readBoolean(KEY_AUTO_DRY, false),
        confirmExit = store.readBoolean(KEY_CONFIRM, true)
    )

    private companion object {
        const val KEY_EXPERT = "setting_expert_boards"
        const val KEY_MARKS = "setting_flavor_marks"
        const val KEY_AUTO_DRY = "setting_auto_dry_run"
        const val KEY_CONFIRM = "setting_confirm_exit"
    }
}
