package com.Famdiba1.Huqsoz.data.sample

import com.Famdiba1.Huqsoz.core.config.GameConfig
import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.ChapterSpec
import com.Famdiba1.Huqsoz.domain.model.GeneratedBoard
import com.Famdiba1.Huqsoz.domain.model.PipeTile
import com.Famdiba1.Huqsoz.domain.model.TileKind
import com.Famdiba1.Huqsoz.domain.repository.BoardFallback

object FallbackBoards : BoardFallback {

    override fun board(spec: ChapterSpec, seed: Long): GeneratedBoard {
        val cols = GameConfig.GRID_COLS
        val rows = GameConfig.GRID_ROWS
        val tiles = MutableList(cols * rows) { PipeTile(TileKind.BLOCKED) }
        val sources = ArrayList<Int>()
        val tanks = ArrayList<Int>()
        val lanes = spec.flavors.size
        val band = cols / lanes

        for (lane in spec.flavors.indices) {
            val flavor = spec.flavors[lane]
            val col = lane * band + band / 2
            val sourceIndex = col
            val tankIndex = (rows - 1) * cols + col
            tiles[sourceIndex] = PipeTile(TileKind.SOURCE, flavor = flavor)
            tiles[tankIndex] = PipeTile(TileKind.TANK, flavor = flavor)
            sources.add(sourceIndex)
            tanks.add(tankIndex)
            for (row in 1 until rows - 1) {
                val rotation = if ((row + lane) % 2 == 0) 1 else 0
                tiles[row * cols + col] = PipeTile(TileKind.STRAIGHT, rotation = rotation)
            }
        }

        for (row in 1 until rows - 1) {
            for (col in 0 until cols) {
                val index = row * cols + col
                if (tiles[index].kind != TileKind.BLOCKED) continue
                tiles[index] = PipeTile(TileKind.ELBOW, rotation = (row + col) % 4)
            }
        }

        val board = Board(cols = cols, rows = rows, tiles = tiles, seed = seed)
        return GeneratedBoard(board = board, plan = spec.plan, sources = sources, tanks = tanks)
    }
}
