package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.CourseReportScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.CoursesHistoryScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.FatigueDetectionScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.StartCourseScreen

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StartCourseScreen.route
    ) {
        composable(route = Screen.StartCourseScreen.route) {
            StartCourseScreen(navController)
        }

        composable(route = Screen.FatigueDetection.route) {
            FatigueDetectionScreen()
        }

        composable(route = Screen.CourseReport.route) {
            CourseReportScreen()
        }

        composable(route = Screen.CoursesHistory.route) {
            CoursesHistoryScreen()
        }

        composable(route = Screen.UserProfile.route) {
            CourseReportScreen()
        }
    }
}