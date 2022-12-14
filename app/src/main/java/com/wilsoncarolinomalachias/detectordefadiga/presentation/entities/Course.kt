package com.wilsoncarolinomalachias.detectordefadiga.presentation.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.wilsoncarolinomalachias.detectordefadiga.presentation.utils.DateConverter
import java.util.*

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true) val uid: Int,

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "start_date")
    val startDate: Date,

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "finish_date")
    val finishDate: Date,

    @ColumnInfo(name = "start_address") val startAddress: String?,

    @ColumnInfo(name = "destination_address") val destinationAddress: String?,

    @ColumnInfo(name = "fatigue_count") val fatigueCount: Int = 0
)
