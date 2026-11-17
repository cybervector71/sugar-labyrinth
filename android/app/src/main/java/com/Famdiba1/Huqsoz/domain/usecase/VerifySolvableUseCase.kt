package com.Famdiba1.Huqsoz.domain.usecase

import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.Direction
import com.Famdiba1.Huqsoz.domain.model.GeneratedBoard
import com.Famdiba1.Huqsoz.domain.model.TileKind

class VerifySolvableUseCase {

    operator fun invoke(generated: GeneratedBoard, moveBudget: Int): Boolean {
        val board = generated.board
        var totalSteps = 0
        for (sourceIndex in generated.sources) {
            val flavor = board.tiles[sourceIndex].flavor ?: return false
            val steps = shortestRun(board, sourceIndex, flavor.ordinal) ?: return false
            totalSteps += steps
        }
        return totalSteps <= moveBudget + board.rows
    }

    private fun shortestRun(board: Board, sourceIndex: Int, flavorOrdinal: Int): Int? {
        val startCol = board.colOf(sourceIndex)
        val startRow = board.rowOf(sourceIndex) + 1
        if (startRow >= board.rows) return null
        val startCell = board.indexOf(startCol, startRow)
        val visited = HashSet<Int>()
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        queue.add(Triple(startCell, Direction.UP, 1))
        visited.add(startCell * 4 + Direction.UP)
        while (queue.isNotEmpty()) {
            val (cell, enterDir, steps) = queue.removeFirst()
            val tile = board.tiles[cell]
            if (tile.kind == TileKind.BLOCKED || tile.kind == TileKind.SOURCE) continue
            if (tile.kind == TileKind.TANK) {
                if (enterDir == Direction.UP && tile.flavor?.ordinal == flavorOrdinal) return steps
                continue
            }
            for (exit in 0 until 4) {
                if (exit == enterDir) continue
                if (!canRoute(tile.kind, enterDir, exit)) continue
                val nextCol = board.colOf(cell) + Direction.DX[exit]
                val nextRow = board.rowOf(cell) + Direction.DY[exit]
                if (nextCol < 0 || nextCol >= board.cols || nextRow < 0 || nextRow >= board.rows) continue
                val next = board.indexOf(nextCol, nextRow)
                val nextEnter = Direction.opposite(exit)
                val key = next * 4 + nextEnter
                if (!visited.add(key)) continue
                queue.add(Triple(next, nextEnter, steps + 1))
            }
        }
        return null
    }

    private fun canRoute(kind: TileKind, enter: Int, exit: Int): Boolean = when (kind) {
        TileKind.STRAIGHT -> exit == Direction.opposite(enter)
        TileKind.ELBOW -> exit != Direction.opposite(enter)
        TileKind.TEE, TileKind.CROSS, TileKind.SWITCH -> true
        else -> false
    }
}
