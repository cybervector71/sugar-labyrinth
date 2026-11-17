package com.Famdiba1.Huqsoz.core.di

import android.content.Context
import com.Famdiba1.Huqsoz.data.local.PreferencesStore
import com.Famdiba1.Huqsoz.data.repository.ProgressRepositoryImpl
import com.Famdiba1.Huqsoz.data.repository.SettingsRepositoryImpl
import com.Famdiba1.Huqsoz.data.sample.FallbackBoards
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import com.Famdiba1.Huqsoz.domain.usecase.GenerateBoardUseCase
import com.Famdiba1.Huqsoz.domain.usecase.RotateTileUseCase
import com.Famdiba1.Huqsoz.domain.usecase.RunPourUseCase
import com.Famdiba1.Huqsoz.domain.usecase.ScoreRoundUseCase
import com.Famdiba1.Huqsoz.domain.usecase.VerifySolvableUseCase

object ServiceLocator {

    private var progressHolder: ProgressRepository? = null
    private var settingsHolder: SettingsRepository? = null

    val generateBoard = GenerateBoardUseCase(VerifySolvableUseCase(), FallbackBoards)
    val runPour = RunPourUseCase()
    val scoreRound = ScoreRoundUseCase()
    val rotateTile = RotateTileUseCase()

    fun init(context: Context) {
        if (progressHolder != null && settingsHolder != null) return
        val store = PreferencesStore(context)
        progressHolder = ProgressRepositoryImpl(store)
        settingsHolder = SettingsRepositoryImpl(store)
    }

    val progressRepository: ProgressRepository
        get() = progressHolder ?: throw IllegalStateException(NOT_READY)

    val settingsRepository: SettingsRepository
        get() = settingsHolder ?: throw IllegalStateException(NOT_READY)

    private const val NOT_READY = "ServiceLocator.init must run before the first screen"
}
