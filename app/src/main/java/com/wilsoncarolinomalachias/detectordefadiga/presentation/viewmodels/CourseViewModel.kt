package com.wilsoncarolinomalachias.detectordefadiga.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsoncarolinomalachias.detectordefadiga.presentation.database.AppDatabase
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course
import com.wilsoncarolinomalachias.detectordefadiga.presentation.repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseViewModel(context: Context): ViewModel() {
    val coursesLiveData: LiveData<List<Course>>
    private val repository: CourseRepository

    init {
        val courseDao = AppDatabase.getDatabase(context).courseDao()
        repository = CourseRepository(courseDao)
        coursesLiveData = repository.readAllData
    }

    fun addCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCourse(course)
        }
    }

    suspend fun getCourse(courseId: Int): List<Course> {
        return repository.getCourse(courseId)
    }
}