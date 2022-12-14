package com.wilsoncarolinomalachias.detectordefadiga.presentation.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM course")
    fun getAll(): LiveData<List<Course>>

    @Query("SELECT * FROM course WHERE uid IN (:courseIds)")
    suspend fun loadAllByIds(courseIds: IntArray): List<Course>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg courses: Course)

    @Delete
    fun delete(course: Course)
}