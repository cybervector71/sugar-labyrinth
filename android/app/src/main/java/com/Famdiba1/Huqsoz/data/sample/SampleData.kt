package com.Famdiba1.Huqsoz.data.sample

import com.Famdiba1.Huqsoz.domain.model.ChapterSpec
import com.Famdiba1.Huqsoz.domain.model.Flavor

object SampleData {

    val flavorOrder: List<Flavor> = listOf(
        Flavor.RASPBERRY,
        Flavor.PISTACHIO,
        Flavor.BLUEBERRY,
        Flavor.CARAMEL,
        Flavor.VANILLA
    )

    val chapters: List<ChapterSpec> = listOf(
        ChapterSpec(
            index = 0,
            flavors = listOf(Flavor.RASPBERRY, Flavor.PISTACHIO),
            plan = mapOf(Flavor.RASPBERRY to 2, Flavor.PISTACHIO to 1),
            blockedCount = 3,
            scrambleChance = 0.45f
        ),
        ChapterSpec(
            index = 1,
            flavors = listOf(Flavor.BLUEBERRY, Flavor.CARAMEL),
            plan = mapOf(Flavor.BLUEBERRY to 2, Flavor.CARAMEL to 2),
            blockedCount = 4,
            scrambleChance = 0.55f
        ),
        ChapterSpec(
            index = 2,
            flavors = listOf(Flavor.CARAMEL, Flavor.VANILLA, Flavor.RASPBERRY),
            plan = mapOf(Flavor.CARAMEL to 2, Flavor.VANILLA to 2, Flavor.RASPBERRY to 1),
            blockedCount = 4,
            scrambleChance = 0.60f
        ),
        ChapterSpec(
            index = 3,
            flavors = listOf(Flavor.PISTACHIO, Flavor.BLUEBERRY, Flavor.VANILLA),
            plan = mapOf(Flavor.PISTACHIO to 2, Flavor.BLUEBERRY to 2, Flavor.VANILLA to 2),
            blockedCount = 5,
            scrambleChance = 0.65f
        )
    )

    fun chapter(index: Int): ChapterSpec = chapters.getOrElse(index) { chapters.first() }
}
