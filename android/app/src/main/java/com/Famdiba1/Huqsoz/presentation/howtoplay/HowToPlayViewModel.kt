package com.Famdiba1.Huqsoz.presentation.howtoplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.data.sample.SampleData
import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HowToPlayUiState(
    val flavors: List<Flavor> = SampleData.flavorOrder,
    val flavorMarks: Boolean = true
)

class HowToPlayViewModel(settingsRepository: SettingsRepository) : ViewModel() {

    val state: StateFlow<HowToPlayUiState> = settingsRepository.settings
        .map { HowToPlayUiState(flavorMarks = it.flavorMarks) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HowToPlayUiState()
        )
}
