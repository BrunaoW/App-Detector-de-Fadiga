package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.CourseReportScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.CoursesHistoryScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.FatigueDetectionScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.login.LoginScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.signUp.SignUpScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.StartCourseScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.translatedata.TranslateDataScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.userprofile.UserProfileScreen

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StartCourse.route,
        modifier = modifier
    ) {
        composable(route = Screen.StartCourse.route) {
            StartCourseScreen(
                navController = navController
            )
        }

        composable(route = Screen.FatigueDetection.route) {
            FatigueDetectionScreen(
                navController = navController
            )
        }

        composable(
            route = "${Screen.CourseReport.route}/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")

            if (courseId.isNullOrEmpty()) {
                Toast
                    .makeText(context, "Id de curso inválido", Toast.LENGTH_LONG)
                    .show()
                navController.navigate(Screen.StartCourse.route)
            } else {
                CourseReportScreen(
                    navController = navController,
                    courseId = courseId
                )
            }
        }

        composable(route = Screen.CoursesHistory.route) {
            CoursesHistoryScreen(
                navController = navController
            )
        }

        composable(route = Screen.TranslateData.route) {
            TranslateDataScreen(
                navController = navController
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                navController = navController
            )
        }

        composable(route = Screen.Signup.route) {
            SignUpScreen(
                navController = navController
            )
        }

        composable(route = Screen.UserProfile.route) {
            UserProfileScreen(
                navController = navController
            )
        }
    }
}