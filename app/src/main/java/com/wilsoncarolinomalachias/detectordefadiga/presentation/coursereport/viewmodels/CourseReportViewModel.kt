package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course

class CourseReportViewModel : ViewModel() {
    var loadedCourse = mutableStateOf<Course?>(null)
        private set

    fun setLoadedCourse(course: Course) {
        loadedCourse.value = course
    }
}