package com.Famdiba1.Huqsoz.domain.repository

import com.Famdiba1.Huqsoz.domain.model.ChapterSpec
import com.Famdiba1.Huqsoz.domain.model.GeneratedBoard

interface BoardFallback {
    fun board(spec: ChapterSpec, seed: Long): GeneratedBoard
}
