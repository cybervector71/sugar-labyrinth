package com.Famdiba1.Huqsoz.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.core.theme.ClayShadow
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.CopperLight
import com.Famdiba1.Huqsoz.core.theme.DisabledFill
import com.Famdiba1.Huqsoz.core.theme.GlassTint
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.FlavorBadge
import com.Famdiba1.Huqsoz.core.ui.flavorColor
import com.Famdiba1.Huqsoz.domain.model.Direction
import com.Famdiba1.Huqsoz.domain.model.PipeTile
import com.Famdiba1.Huqsoz.domain.model.TileKind

@Composable
fun BoardGrid(
    state: GameUiState,
    tileSize: Dp,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (row in 0 until state.board.rows) {
            Row {
                for (col in 0 until state.board.cols) {
                    val index = row * state.board.cols + col
                    val tile = state.board.tiles[index]
                    val cell = state.flow[index]
                    val wet = cell != null && cell.distance <= state.fillDistance
                    val fillColor = if (wet) cell?.flavor?.let { flavorColor(it) } else null
                    val tankLevel = if (tile.kind == TileKind.TANK && tile.flavor != null) {
                        val required = state.plan[tile.flavor] ?: 0
                        val done = state.delivered[tile.flavor] ?: 0
                        if (required <= 0) 0f else (done.toFloat() / required.toFloat()).coerceIn(0f, 1f)
                    } else {
                        0f
                    }
                    TileCell(
                        tile = tile,
                        tileSize = tileSize,
                        fillColor = fillColor,
                        ghost = state.ghost,
                        spoils = cell?.spoils == true && wet,
                        shaking = state.shakeCell == index,
                        showMark = state.flavorMarks,
                        tankLevel = tankLevel,
                        onTap = { onTap(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TileCell(
    tile: PipeTile,
    tileSize: Dp,
    fillColor: Color?,
    ghost: Boolean,
    spoils: Boolean,
    shaking: Boolean,
    showMark: Boolean,
    tankLevel: Float,
    onTap: () -> Unit
) {
    val angle by animateFloatAsState(
        targetValue = tile.rotation * 90f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 420f),
        label = "tileSpin"
    )
    val nudge by animateFloatAsState(
        targetValue = if (shaking) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.35f, stiffness = 1400f),
        label = "tileNudge"
    )
    val body = when (tile.kind) {
        TileKind.BLOCKED -> DisabledFill
        TileKind.SOURCE -> CopperLight.copy(alpha = 0.55f)
        TileKind.TANK -> GlassTint.copy(alpha = 0.75f)
        else -> VanillaCream.copy(alpha = 0.92f)
    }
    Box(
        modifier = Modifier
            .size(tileSize)
            .padding(2.dp)
            .graphicsLayer {
                scaleX = nudge
                scaleY = nudge
            }
            .background(color = body, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Copper.copy(alpha = if (tile.interactive) 0.4f else 0.22f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = true, onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (tile.kind) {
                TileKind.BLOCKED -> drawBoiler()
                TileKind.SOURCE -> drawNozzle(tile.flavor?.let { flavorColor(it) } ?: Copper)
                TileKind.TANK -> drawTank(
                    tile.flavor?.let { flavorColor(it) } ?: Copper,
                    tankLevel,
                    spoils
                )
                else -> {
                    val alpha = if (ghost) 0.78f else 1f
                    rotate(degrees = angle) {
                        drawStubs(tile.stubOpenings)
                        drawPipe(tile.baseOpenings, fillColor, alpha)
                    }
                }
            }
        }
        if (tile.flavor != null && (tile.kind == TileKind.SOURCE || tile.kind == TileKind.TANK)) {
            FlavorBadge(
                flavor = tile.flavor,
                size = tileSize * 0.42f,
                showMark = showMark
            )
        }
    }
}

private fun DrawScope.edgePoint(dir: Int): Offset = when (dir) {
    Direction.UP -> Offset(size.width / 2f, 0f)
    Direction.RIGHT -> Offset(size.width, size.height / 2f)
    Direction.DOWN -> Offset(size.width / 2f, size.height)
    else -> Offset(0f, size.height / 2f)
}

private fun DrawScope.drawPipe(dirs: Set<Int>, fillColor: Color?, alpha: Float) {
    if (dirs.isEmpty()) return
    val center = Offset(size.width / 2f, size.height / 2f)
    val outer = size.minDimension * 0.30f
    val inner = outer * 0.48f
    for (dir in dirs) {
        drawLine(
            color = Copper.copy(alpha = alpha),
            start = center,
            end = edgePoint(dir),
            strokeWidth = outer,
            cap = StrokeCap.Butt
        )
    }
    drawCircle(color = Copper.copy(alpha = alpha), radius = outer / 2f, center = center)
    val bore = fillColor ?: VanillaCream
    for (dir in dirs) {
        drawLine(
            color = bore.copy(alpha = alpha),
            start = center,
            end = edgePoint(dir),
            strokeWidth = inner,
            cap = StrokeCap.Butt
        )
    }
    drawCircle(color = bore.copy(alpha = alpha), radius = inner / 2f, center = center)
    if (fillColor != null) {
        for (dir in dirs) {
            drawLine(
                color = SurfaceLight.copy(alpha = 0.35f),
                start = center,
                end = edgePoint(dir),
                strokeWidth = inner * 0.3f,
                cap = StrokeCap.Butt
            )
        }
    }
}

private fun DrawScope.drawStubs(stubs: Set<Int>) {
    if (stubs.isEmpty()) return
    val center = Offset(size.width / 2f, size.height / 2f)
    val outer = size.minDimension * 0.18f
    for (dir in stubs) {
        val target = edgePoint(dir)
        drawLine(
            color = Copper.copy(alpha = 0.3f),
            start = center,
            end = Offset(
                center.x + (target.x - center.x) * 0.72f,
                center.y + (target.y - center.y) * 0.72f
            ),
            strokeWidth = outer,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawBoiler() {
    val inset = size.minDimension * 0.2f
    drawRoundRect(
        color = ClayShadow.copy(alpha = 0.45f),
        topLeft = Offset(inset, inset),
        size = Size(size.width - inset * 2f, size.height - inset * 2f),
        cornerRadius = CornerRadius(inset * 0.6f, inset * 0.6f)
    )
    drawRoundRect(
        color = Copper.copy(alpha = 0.55f),
        topLeft = Offset(inset, inset),
        size = Size(size.width - inset * 2f, size.height - inset * 2f),
        cornerRadius = CornerRadius(inset * 0.6f, inset * 0.6f),
        style = Stroke(width = size.minDimension * 0.06f)
    )
}

private fun DrawScope.drawNozzle(accent: Color) {
    val width = size.width * 0.44f
    val height = size.height * 0.4f
    drawRoundRect(
        color = Copper,
        topLeft = Offset((size.width - width) / 2f, 0f),
        size = Size(width, height),
        cornerRadius = CornerRadius(width * 0.25f, width * 0.25f)
    )
    drawCircle(
        color = accent,
        radius = size.minDimension * 0.1f,
        center = Offset(size.width / 2f, height + size.minDimension * 0.12f)
    )
    val center = Offset(size.width / 2f, size.height / 2f)
    drawLine(
        color = Copper,
        start = center,
        end = Offset(size.width / 2f, size.height),
        strokeWidth = size.minDimension * 0.26f,
        cap = StrokeCap.Butt
    )
    drawLine(
        color = accent,
        start = center,
        end = Offset(size.width / 2f, size.height),
        strokeWidth = size.minDimension * 0.12f,
        cap = StrokeCap.Butt
    )
}

private fun DrawScope.drawTank(accent: Color, level: Float, spoils: Boolean) {
    val inset = size.minDimension * 0.16f
    val top = size.height * 0.26f
    val bodyHeight = size.height - top - inset * 0.4f
    val bodyWidth = size.width - inset * 2f
    drawLine(
        color = Copper,
        start = Offset(size.width / 2f, 0f),
        end = Offset(size.width / 2f, top),
        strokeWidth = size.minDimension * 0.24f,
        cap = StrokeCap.Butt
    )
    drawRoundRect(
        color = SurfaceLight.copy(alpha = 0.45f),
        topLeft = Offset(inset, top),
        size = Size(bodyWidth, bodyHeight),
        cornerRadius = CornerRadius(inset, inset)
    )
    if (level > 0f) {
        val liquidHeight = bodyHeight * level
        drawRoundRect(
            color = accent.copy(alpha = 0.85f),
            topLeft = Offset(inset, top + bodyHeight - liquidHeight),
            size = Size(bodyWidth, liquidHeight),
            cornerRadius = CornerRadius(inset * 0.6f, inset * 0.6f)
        )
    }
    drawRoundRect(
        color = if (spoils) accent else SurfaceLight.copy(alpha = 0.8f),
        topLeft = Offset(inset, top),
        size = Size(bodyWidth, bodyHeight),
        cornerRadius = CornerRadius(inset, inset),
        style = Stroke(width = size.minDimension * 0.07f)
    )
}
