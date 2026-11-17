package com.Famdiba1.Huqsoz.domain.usecase

import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.Direction
import com.Famdiba1.Huqsoz.domain.model.Flavor
import com.Famdiba1.Huqsoz.domain.model.TileKind

data class FlowCell(val distance: Int, val flavor: Flavor, val spoils: Boolean)

data class PourResult(
    val flow: Map<Int, FlowCell>,
    val delivered: Map<Flavor, Int>,
    val spoiledBy: Flavor?,
    val maxDistance: Int
)

class RunPourUseCase {

    operator fun invoke(board: Board): PourResult {
        val flow = HashMap<Int, FlowCell>()
        val delivered = HashMap<Flavor, Int>()
        var spoiledBy: Flavor? = null
        var maxDistance = 0

        for (sourceIndex in board.tiles.indices) {
            val source = board.tiles[sourceIndex]
            if (source.kind != TileKind.SOURCE) continue
            val flavor = source.flavor ?: continue
            if (flow.containsKey(sourceIndex)) continue
            flow[sourceIndex] = FlowCell(0, flavor, false)
            val queue = ArrayDeque<Int>()
            val distance = HashMap<Int, Int>()
            queue.add(sourceIndex)
            distance[sourceIndex] = 0
            while (queue.isNotEmpty()) {
                val cell = queue.removeFirst()
                val step = distance[cell] ?: 0
                val tile = board.tiles[cell]
                if (tile.kind == TileKind.TANK) continue
                for (dir in tile.openings) {
                    val nextCol = board.colOf(cell) + Direction.DX[dir]
                    val nextRow = board.rowOf(cell) + Direction.DY[dir]
                    if (nextCol < 0 || nextCol >= board.cols || nextRow < 0 || nextRow >= board.rows) continue
                    val next = board.indexOf(nextCol, nextRow)
                    val nextTile = board.tiles[next]
                    if (nextTile.kind == TileKind.SOURCE) continue
                    if (!nextTile.openings.contains(Direction.opposite(dir))) continue
                    if (flow.containsKey(next)) continue
                    val nextStep = step + 1
                    val wrongTank = nextTile.kind == TileKind.TANK && nextTile.flavor != flavor
                    flow[next] = FlowCell(nextStep, flavor, wrongTank)
                    distance[next] = nextStep
                    if (nextStep > maxDistance) maxDistance = nextStep
                    if (nextTile.kind == TileKind.TANK) {
                        if (wrongTank) {
                            if (spoiledBy == null) spoiledBy = flavor
                        } else {
                            delivered[flavor] = (delivered[flavor] ?: 0) + 1
                        }
                    } else {
                        queue.add(next)
                    }
                }
            }
        }

        return PourResult(
            flow = flow,
            delivered = delivered,
            spoiledBy = spoiledBy,
            maxDistance = maxDistance
        )
    }
}
