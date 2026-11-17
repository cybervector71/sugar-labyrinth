package com.Famdiba1.Huqsoz.domain.model

data class Board(
    val cols: Int,
    val rows: Int,
    val tiles: List<PipeTile>,
    val seed: Long
) {
    fun indexOf(col: Int, row: Int): Int = row * cols + col

    fun colOf(index: Int): Int = index % cols

    fun rowOf(index: Int): Int = index / cols

    fun withTile(index: Int, tile: PipeTile): Board {
        val next = tiles.toMutableList()
        next[index] = tile
        return copy(tiles = next)
    }

    companion object {
        val Empty = Board(cols = 0, rows = 0, tiles = emptyList(), seed = 0L)
    }
}

data class GeneratedBoard(
    val board: Board,
    val plan: Map<Flavor, Int>,
    val sources: List<Int>,
    val tanks: List<Int>
)

data class ChapterSpec(
    val index: Int,
    val flavors: List<Flavor>,
    val plan: Map<Flavor, Int>,
    val blockedCount: Int,
    val scrambleChance: Float
)

data class ProductionPlan(val required: Map<Flavor, Int>) {
    val total: Int get() = required.values.sum()
}
