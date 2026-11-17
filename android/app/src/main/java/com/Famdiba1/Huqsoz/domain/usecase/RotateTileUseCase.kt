package com.Famdiba1.Huqsoz.domain.usecase

import com.Famdiba1.Huqsoz.domain.model.Board
import com.Famdiba1.Huqsoz.domain.model.TileKind

class RotateTileUseCase {

    operator fun invoke(board: Board, index: Int): Board {
        if (index < 0 || index >= board.tiles.size) return board
        val tile = board.tiles[index]
        return when {
            tile.rotatable -> board.withTile(index, tile.rotated())
            tile.kind == TileKind.SWITCH -> board.withTile(index, tile.flipped())
            else -> board
        }
    }
}
