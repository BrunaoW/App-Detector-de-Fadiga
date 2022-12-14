package com.wilsoncarolinomalachias.detectordefadiga.presentation.repository

import androidx.lifecycle.LiveData
import com.wilsoncarolinomalachias.detectordefadiga.presentation.dao.CourseDao
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course

class CourseRepository(private val courseDao: CourseDao) {

    val readAllData: LiveData<List<Course>> = courseDao.getAll()

    suspend fun addCourse(course: Course) {
        courseDao.insertAll(course)
    }
}