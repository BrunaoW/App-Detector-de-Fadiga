package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.CourseReportScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.CoursesHistoryScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.FatigueDetectionScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.StartCourseScreen

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StartCourseScreen.route,
        modifier = modifier
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