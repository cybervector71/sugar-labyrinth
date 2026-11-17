package com.Famdiba1.Huqsoz.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.core.config.GameConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val state = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            state.value = true
        }
    }
}
