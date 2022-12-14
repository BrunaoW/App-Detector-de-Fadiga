package com.wilsoncarolinomalachias.detectordefadiga.presentation.utils

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}