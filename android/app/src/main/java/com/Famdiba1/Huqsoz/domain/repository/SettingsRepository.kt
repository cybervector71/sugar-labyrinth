package com.Famdiba1.Huqsoz.domain.repository

import com.Famdiba1.Huqsoz.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>

    fun update(settings: GameSettings)
}
