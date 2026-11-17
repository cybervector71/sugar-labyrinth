package com.Famdiba1.Huqsoz.domain.model

enum class TileKind {
    STRAIGHT,
    ELBOW,
    TEE,
    CROSS,
    SWITCH,
    BLOCKED,
    SOURCE,
    TANK
}

data class PipeTile(
    val kind: TileKind,
    val rotation: Int = 0,
    val switchAlt: Boolean = false,
    val flavor: Flavor? = null
) {
    val rotatable: Boolean
        get() = kind == TileKind.STRAIGHT || kind == TileKind.ELBOW ||
            kind == TileKind.TEE || kind == TileKind.CROSS

    val interactive: Boolean
        get() = rotatable || kind == TileKind.SWITCH

    val baseOpenings: Set<Int>
        get() = when (kind) {
            TileKind.STRAIGHT -> setOf(Direction.UP, Direction.DOWN)
            TileKind.ELBOW -> setOf(Direction.UP, Direction.RIGHT)
            TileKind.TEE -> setOf(Direction.UP, Direction.RIGHT, Direction.DOWN)
            TileKind.CROSS -> setOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
            TileKind.SWITCH ->
                if (switchAlt) setOf(Direction.UP, Direction.DOWN) else setOf(Direction.UP, Direction.RIGHT)
            TileKind.BLOCKED -> emptySet()
            TileKind.SOURCE -> setOf(Direction.DOWN)
            TileKind.TANK -> setOf(Direction.UP)
        }

    val stubOpenings: Set<Int>
        get() = if (kind == TileKind.SWITCH) {
            setOf(if (switchAlt) Direction.RIGHT else Direction.DOWN)
        } else {
            emptySet()
        }

    val openings: Set<Int>
        get() = if (kind == TileKind.SOURCE || kind == TileKind.TANK || kind == TileKind.BLOCKED) {
            baseOpenings
        } else {
            baseOpenings.map { (it + rotation) % 4 }.toSet()
        }

    fun rotated(): PipeTile = if (rotatable) copy(rotation = rotation + 1) else this

    fun flipped(): PipeTile = if (kind == TileKind.SWITCH) copy(switchAlt = !switchAlt) else this
}
