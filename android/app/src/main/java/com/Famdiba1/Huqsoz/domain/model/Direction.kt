package com.Famdiba1.Huqsoz.domain.model

object Direction {
    const val UP = 0
    const val RIGHT = 1
    const val DOWN = 2
    const val LEFT = 3

    val DX = intArrayOf(0, 1, 0, -1)
    val DY = intArrayOf(-1, 0, 1, 0)

    fun opposite(dir: Int): Int = (dir + 2) % 4
}
