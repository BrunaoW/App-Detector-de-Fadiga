package com.wilsoncarolinomalachias.detectordefadiga.presentation

sealed class Screen(val route: String) {
    object StartCourseScreen: Screen("start_course_screen")
    object FatigueDetection: Screen("fatigue_detection_screen")
    object CourseReport: Screen("course_report_screen")
    object CoursesHistory: Screen("courses_history_screen")
    object UserProfile: Screen("user_profile_screen")
}