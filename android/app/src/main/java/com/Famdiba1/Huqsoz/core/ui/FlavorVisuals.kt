package com.Famdiba1.Huqsoz.core.ui

import androidx.compose.ui.graphics.Color
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.theme.Blueberry
import com.Famdiba1.Huqsoz.core.theme.Caramel
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.VanillaSyrup
import com.Famdiba1.Huqsoz.domain.model.Flavor

enum class FlavorMark {
    CIRCLE,
    TRIANGLE,
    SQUARE,
    DIAMOND,
    HEXAGON
}

fun flavorColor(flavor: Flavor): Color = when (flavor) {
    Flavor.RASPBERRY -> Raspberry
    Flavor.PISTACHIO -> Pistachio
    Flavor.BLUEBERRY -> Blueberry
    Flavor.CARAMEL -> Caramel
    Flavor.VANILLA -> VanillaSyrup
}

fun flavorDrawable(flavor: Flavor): Int = when (flavor) {
    Flavor.RASPBERRY -> R.drawable.sprite_1
    Flavor.PISTACHIO -> R.drawable.sprite_2
    Flavor.BLUEBERRY -> R.drawable.sprite_3
    Flavor.CARAMEL -> R.drawable.sprite_4
    Flavor.VANILLA -> R.drawable.sprite_5
}

fun flavorMark(flavor: Flavor): FlavorMark = when (flavor) {
    Flavor.RASPBERRY -> FlavorMark.CIRCLE
    Flavor.PISTACHIO -> FlavorMark.TRIANGLE
    Flavor.BLUEBERRY -> FlavorMark.SQUARE
    Flavor.CARAMEL -> FlavorMark.DIAMOND
    Flavor.VANILLA -> FlavorMark.HEXAGON
}

fun flavorLabel(flavor: Flavor): Int = when (flavor) {
    Flavor.RASPBERRY -> R.string.flavor_raspberry
    Flavor.PISTACHIO -> R.string.flavor_pistachio
    Flavor.BLUEBERRY -> R.string.flavor_blueberry
    Flavor.CARAMEL -> R.string.flavor_caramel
    Flavor.VANILLA -> R.string.flavor_vanilla
}

fun groupedNumber(value: Int): String {
    val digits = value.toString()
    if (digits.length <= 3) return digits
    val builder = StringBuilder()
    val offset = digits.length % 3
    for (index in digits.indices) {
        if (index > 0 && (index - offset) % 3 == 0) builder.append(' ')
        builder.append(digits[index])
    }
    return builder.toString()
}
