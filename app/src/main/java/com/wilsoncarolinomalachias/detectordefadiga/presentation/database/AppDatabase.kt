package com.wilsoncarolinomalachias.detectordefadiga.presentation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wilsoncarolinomalachias.detectordefadiga.presentation.dao.CourseDao
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course
import com.wilsoncarolinomalachias.detectordefadiga.presentation.utils.DateConverter

@Database(entities = [Course::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-course"
                ).build()
                INSTANCE = instance
                return INSTANCE as AppDatabase
            }
        }
    }
}