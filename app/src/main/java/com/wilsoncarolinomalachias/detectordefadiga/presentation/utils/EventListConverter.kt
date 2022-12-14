package com.wilsoncarolinomalachias.detectordefadiga.presentation.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object EventListConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Pair<Long, String>> {
        val eventListType: Type = object : TypeToken<ArrayList<Pair<Long, String>>>() {}.type
        return Gson().fromJson(value, eventListType)
    }

    @TypeConverter
    fun fromArrayList(eventList: ArrayList<Pair<Long, String>>): String {
        val gson = Gson()
        return gson.toJson(eventList)
    }
}