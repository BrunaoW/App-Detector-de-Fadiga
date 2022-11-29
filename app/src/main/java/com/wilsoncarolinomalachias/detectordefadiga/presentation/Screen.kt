package com.wilsoncarolinomalachias.detectordefadiga.presentation

import com.wilsoncarolinomalachias.detectordefadiga.R

sealed class Screen(val route: String, val iconResourceId: Int? = null) {
    object StartCourseScreen: Screen("start_course_screen", R.drawable.ic_directions_car)
    object FatigueDetection: Screen("fatigue_detection_screen", R.drawable.ic_directions_car)
    object CourseReport: Screen("course_report_screen", R.drawable.ic_paper)
    object CoursesHistory: Screen("courses_history_screen", R.drawable.ic_paper)
    object UserProfile: Screen("user_profile_screen", R.drawable.ic_profile)

    companion object {
        fun getAllScreens(): List<Screen> {
            return listOf(
                StartCourseScreen,
                FatigueDetection,
                CourseReport,
                CoursesHistory,
                UserProfile
            )
        }
    }
}