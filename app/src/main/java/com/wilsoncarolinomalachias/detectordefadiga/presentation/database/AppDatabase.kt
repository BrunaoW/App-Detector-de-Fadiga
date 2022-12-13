package com.wilsoncarolinomalachias.detectordefadiga.presentation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wilsoncarolinomalachias.detectordefadiga.presentation.dao.CourseDao
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course

@Database(entities = [Course::class], version = 1)
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
                    context,
                    AppDatabase::class.java,
                    "database-course"
                ).build()
                INSTANCE = instance
                return INSTANCE as AppDatabase
            }
        }
    }
}