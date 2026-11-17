package com.Famdiba1.Huqsoz.domain.usecase

import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.ChapterSpec
import com.Famdiba1.Huqsoz.domain.model.GeneratedBoard
import com.Famdiba1.Huqsoz.domain.model.PipeTile
import com.Famdiba1.Huqsoz.domain.model.TileKind
import com.Famdiba1.Huqsoz.domain.repository.BoardFallback
import kotlin.random.Random

class GenerateBoardUseCase(
    private val verify: VerifySolvableUseCase,
    private val fallback: BoardFallback
) {

    operator fun invoke(
        spec: ChapterSpec,
        boardIndex: Int,
        expert: Boolean,
        attempt: Int,
        moveBudget: Int
    ): GeneratedBoard {
        val salt = if (expert) attempt.toLong() * 2654435761L else 0L
        val baseSeed = spec.index.toLong() * 7919L xor (boardIndex.toLong() * 104729L) xor salt
        for (tryNo in 0 until GameConfig.GENERATE_ATTEMPTS) {
            val candidate = build(spec, baseSeed + tryNo * 31L)
            if (verify(candidate, moveBudget)) return candidate
        }
        return fallback.board(spec, baseSeed)
    }

    private fun build(spec: ChapterSpec, seed: Long): GeneratedBoard {
        val rng = Random(seed)
        val cols = GameConfig.GRID_COLS
        val rows = GameConfig.GRID_ROWS
        val tiles = MutableList(cols * rows) { PipeTile(TileKind.BLOCKED) }
        val carved = HashSet<Int>()
        val sources = ArrayList<Int>()
        val tanks = ArrayList<Int>()
        val lanes = spec.flavors.size
        val band = cols / lanes

        for (lane in spec.flavors.indices) {
            val flavor = spec.flavors[lane]
            val start = lane * band
            val sourceCol = start + rng.nextInt(band)
            val tankCol = start + rng.nextInt(band)
            val turnRow = 1 + rng.nextInt(rows - 3)

            val sourceIndex = sourceCol
            val tankIndex = (rows - 1) * cols + tankCol
            tiles[sourceIndex] = PipeTile(TileKind.SOURCE, flavor = flavor)
            tiles[tankIndex] = PipeTile(TileKind.TANK, flavor = flavor)
            sources.add(sourceIndex)
            tanks.add(tankIndex)

            for (row in 1 until turnRow) {
                place(tiles, carved, row * cols + sourceCol, PipeTile(TileKind.STRAIGHT, rotation = 0))
            }
            if (sourceCol == tankCol) {
                place(tiles, carved, turnRow * cols + sourceCol, PipeTile(TileKind.STRAIGHT, rotation = 0))
            } else {
                val goingRight = tankCol > sourceCol
                place(
                    tiles,
                    carved,
                    turnRow * cols + sourceCol,
                    PipeTile(TileKind.ELBOW, rotation = if (goingRight) 0 else 3)
                )
                val from = minOf(sourceCol, tankCol) + 1
                val until = maxOf(sourceCol, tankCol)
                for (col in from until until) {
                    place(tiles, carved, turnRow * cols + col, PipeTile(TileKind.STRAIGHT, rotation = 1))
                }
                place(
                    tiles,
                    carved,
                    turnRow * cols + tankCol,
                    PipeTile(TileKind.ELBOW, rotation = if (goingRight) 2 else 1)
                )
            }
            for (row in turnRow + 1 until rows - 1) {
                place(tiles, carved, row * cols + tankCol, PipeTile(TileKind.STRAIGHT, rotation = 0))
            }
        }

        val fillers = ArrayList<Int>()
        for (row in 1 until rows - 1) {
            for (col in 0 until cols) {
                val index = row * cols + col
                if (carved.contains(index)) continue
                fillers.add(index)
            }
        }
        fillers.shuffleWith(rng)
        val blocked = minOf(spec.blockedCount, fillers.size)
        for (position in fillers.indices) {
            val index = fillers[position]
            tiles[index] = if (position < blocked) {
                PipeTile(TileKind.BLOCKED)
            } else {
                PipeTile(randomKind(rng), rotation = rng.nextInt(4), switchAlt = rng.nextBoolean())
            }
        }

        for (index in carved) {
            val tile = tiles[index]
            if (rng.nextFloat() < spec.scrambleChance) {
                tiles[index] = tile.copy(rotation = rng.nextInt(4))
            }
        }

        val board = Board(cols = cols, rows = rows, tiles = tiles, seed = seed)
        return GeneratedBoard(board = board, plan = spec.plan, sources = sources, tanks = tanks)
    }

    private fun place(tiles: MutableList<PipeTile>, carved: MutableSet<Int>, index: Int, tile: PipeTile) {
        tiles[index] = tile
        carved.add(index)
    }

    private fun randomKind(rng: Random): TileKind {
        val roll = rng.nextInt(100)
        return when {
            roll < 32 -> TileKind.STRAIGHT
            roll < 62 -> TileKind.ELBOW
            roll < 76 -> TileKind.TEE
            roll < 86 -> TileKind.CROSS
            roll < 95 -> TileKind.SWITCH
            else -> TileKind.BLOCKED
        }
    }
}

private fun <T> MutableList<T>.shuffleWith(rng: Random) {
    for (i in size - 1 downTo 1) {
        val j = rng.nextInt(i + 1)
        val tmp = this[i]
        this[i] = this[j]
        this[j] = tmp
    }
}

