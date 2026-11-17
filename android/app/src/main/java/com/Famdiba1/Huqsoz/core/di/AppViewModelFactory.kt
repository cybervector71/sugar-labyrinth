package com.Famdiba1.Huqsoz.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Famdiba1.Huqsoz.presentation.app.AppViewModel
import com.Famdiba1.Huqsoz.presentation.game.GameViewModel
import com.Famdiba1.Huqsoz.presentation.gameover.GameOverViewModel
import com.Famdiba1.Huqsoz.presentation.howtoplay.HowToPlayViewModel
import com.Famdiba1.Huqsoz.presentation.levelselect.LevelSelectViewModel
import com.Famdiba1.Huqsoz.presentation.menu.MenuViewModel
import com.Famdiba1.Huqsoz.presentation.settings.SettingsViewModel
import com.Famdiba1.Huqsoz.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.settingsRepository
            )
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) -> LevelSelectViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.settingsRepository
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                ServiceLocator.progressRepository
            )
            modelClass.isAssignableFrom(HowToPlayViewModel::class.java) -> HowToPlayViewModel(
                ServiceLocator.settingsRepository
            )
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.settingsRepository
            )
            else -> throw IllegalArgumentException(UNKNOWN + modelClass.name)
        }
        return created as T
    }
}

class GameViewModelFactory(
    private val chapter: Int,
    private val board: Int,
    private val expert: Boolean
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (!modelClass.isAssignableFrom(GameViewModel::class.java)) {
            throw IllegalArgumentException(UNKNOWN + modelClass.name)
        }
        val created = GameViewModel(
            chapter = chapter,
            boardIndex = board,
            expertRequested = expert,
            progressRepository = ServiceLocator.progressRepository,
            settingsRepository = ServiceLocator.settingsRepository,
            generateBoard = ServiceLocator.generateBoard,
            runPour = ServiceLocator.runPour,
            scoreRound = ServiceLocator.scoreRound,
            rotateTile = ServiceLocator.rotateTile
        )
        return created as T
    }
}

private const val UNKNOWN = "Unsupported ViewModel: "
