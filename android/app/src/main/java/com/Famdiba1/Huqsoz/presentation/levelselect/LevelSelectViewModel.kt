package com.Famdiba1.Huqsoz.presentation.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.domain.repository.ProgressRepository
import com.Famdiba1.Huqsoz.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LevelSelectViewModel(
    progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val selected = MutableStateFlow(0)

    val state: StateFlow<LevelSelectUiState> = combine(
        progressRepository.progress,
        settingsRepository.settings,
        selected
    ) { progress, settings, chapter ->
        val tabs = (0 until GameConfig.CHAPTERS).map { index ->
            ChapterTab(
                index = index,
                stars = progress.chapterStars(index),
                unlocked = progress.chapterUnlocked(index)
            )
        }
        val unlocked = progress.chapterUnlocked(chapter)
        val boards = (0 until GameConfig.BOARDS_PER_CHAPTER).map { board ->
            BoardCell(
                chapter = chapter,
                board = board,
                stars = progress.starsOf(chapter, board),
                unlocked = unlocked
            )
        }
        LevelSelectUiState(
            selected = chapter,
            tabs = tabs,
            boards = boards,
            expertUnlocked = unlocked && progress.chapterCleared(chapter),
            expertBoards = settings.expertBoards
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LevelSelectUiState()
    )

    fun selectChapter(index: Int) {
        selected.value = index
    }
}
