package com.Famdiba1.Huqsoz.presentation.app

import androidx.lifecycle.ViewModel
import com.Famdiba1.Huqsoz.core.navigation.Screen
import com.Famdiba1.Huqsoz.domain.model.RoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = current.asStateFlow()

    private val backAvailable = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = backAvailable.asStateFlow()

    fun open(target: Screen) {
        backStack.addLast(current.value)
        current.value = target
        sync()
    }

    fun home() {
        backStack.clear()
        current.value = Screen.Menu
        sync()
    }

    private var session = 0

    fun openGame(chapter: Int, board: Int, expert: Boolean) {
        session += 1
        backStack.clear()
        backStack.addLast(Screen.Menu)
        current.value = Screen.Game(chapter, board, expert, session)
        sync()
    }

    fun showResult(result: RoundResult) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        current.value = Screen.GameOver(result)
        sync()
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        current.value = previous
        sync()
        return true
    }

    private fun sync() {
        backAvailable.value = backStack.isNotEmpty()
    }
}
