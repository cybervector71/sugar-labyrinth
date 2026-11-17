package com.Famdiba1.Huqsoz.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.navigation.Screen
import com.Famdiba1.Huqsoz.presentation.game.GameScreen
import com.Famdiba1.Huqsoz.presentation.gameover.GameOverScreen
import com.Famdiba1.Huqsoz.presentation.howtoplay.HowToPlayScreen
import com.Famdiba1.Huqsoz.presentation.levelselect.LevelSelectScreen
import com.Famdiba1.Huqsoz.presentation.menu.MenuScreen
import com.Famdiba1.Huqsoz.presentation.settings.SettingsScreen
import com.Famdiba1.Huqsoz.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            when (targetState) {
                is Screen.Game ->
                    (slideInHorizontally(animationSpec = tween(durationMillis = 300)) { full -> full / 3 } +
                        fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 220))
                is Screen.GameOver ->
                    (scaleIn(initialScale = 0.88f, animationSpec = tween(durationMillis = 360)) +
                        fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 240))
                else ->
                    fadeIn(animationSpec = tween(durationMillis = 420)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 320))
            }
        },
        label = "appScreen"
    ) { current ->
        when (current) {
            Screen.Splash -> SplashScreen(onFinished = appViewModel::home)
            Screen.Menu -> MenuScreen(
                onPlay = { chapter, board -> appViewModel.openGame(chapter, board, false) },
                onLevels = { appViewModel.open(Screen.LevelSelect) },
                onHowToPlay = { appViewModel.open(Screen.HowToPlay) },
                onSettings = { appViewModel.open(Screen.Settings) }
            )
            Screen.LevelSelect -> LevelSelectScreen(
                onPlayBoard = { chapter, board, expert ->
                    appViewModel.openGame(chapter, board, expert)
                },
                onMenu = appViewModel::home
            )
            Screen.HowToPlay -> HowToPlayScreen(onMenu = appViewModel::home)
            Screen.Settings -> SettingsScreen(onMenu = appViewModel::home)
            is Screen.Game -> GameScreen(
                chapter = current.chapter,
                board = current.board,
                expert = current.expert,
                session = current.session,
                onGameOver = appViewModel::showResult,
                onMenu = appViewModel::home
            )
            is Screen.GameOver -> GameOverScreen(
                result = current.result,
                onPlayAgain = {
                    appViewModel.openGame(
                        current.result.chapter,
                        current.result.board,
                        current.result.expert
                    )
                },
                onNextBoard = {
                    appViewModel.openGame(
                        current.result.chapter,
                        current.result.board + 1,
                        current.result.expert
                    )
                },
                onMenu = appViewModel::home
            )
        }
    }
}
