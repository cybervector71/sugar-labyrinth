package com.Famdiba1.Huqsoz.domain.model

import com.Famdiba1.Huqsoz.core.config.GameConfig

data class Progress(
    val boardStars: List<Int> = List(GameConfig.TOTAL_BOARDS) { 0 },
    val bestScore: Int = 0,
    val tokens: Int = 0
) {
    val totalStars: Int get() = boardStars.sum()

    fun starsOf(chapter: Int, board: Int): Int {
        val index = chapter * GameConfig.BOARDS_PER_CHAPTER + board
        return boardStars.getOrElse(index) { 0 }
    }

    fun chapterStars(chapter: Int): Int {
        val from = chapter * GameConfig.BOARDS_PER_CHAPTER
        val until = from + GameConfig.BOARDS_PER_CHAPTER
        return boardStars.subList(from.coerceAtMost(boardStars.size), until.coerceAtMost(boardStars.size)).sum()
    }

    fun chapterCleared(chapter: Int): Boolean {
        val from = chapter * GameConfig.BOARDS_PER_CHAPTER
        return (0 until GameConfig.BOARDS_PER_CHAPTER).all { boardStars.getOrElse(from + it) { 0 } > 0 }
    }

    fun chapterUnlocked(chapter: Int): Boolean =
        chapter == 0 || chapterStars(chapter - 1) >= GameConfig.CHAPTER_STAR_GATE

    fun nextBoard(): Pair<Int, Int> {
        for (chapter in 0 until GameConfig.CHAPTERS) {
            if (!chapterUnlocked(chapter)) continue
            for (board in 0 until GameConfig.BOARDS_PER_CHAPTER) {
                if (starsOf(chapter, board) == 0) return chapter to board
            }
        }
        return 0 to 0
    }
}

data class GameSettings(
    val expertBoards: Boolean = false,
    val flavorMarks: Boolean = true,
    val autoDryRun: Boolean = false,
    val confirmExit: Boolean = true
)
