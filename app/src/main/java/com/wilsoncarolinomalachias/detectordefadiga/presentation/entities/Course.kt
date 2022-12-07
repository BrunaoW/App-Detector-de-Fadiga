package com.wilsoncarolinomalachias.detectordefadiga.presentation.entities

import java.util.*

data class Course (
    val startDate: Date,
    val finishDate: Date,
    val startAddress: String,
    val destinationAddress: String,
    val fatigueCount: Int = 0
)
