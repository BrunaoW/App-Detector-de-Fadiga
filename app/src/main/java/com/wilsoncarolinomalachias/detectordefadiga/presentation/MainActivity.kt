package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.BottomNav
import com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.CourseReportScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.CoursesHistoryScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.FatigueDetectionScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.StartCourseScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetectorDeFadigaTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    
                    Scaffold(
                        bottomBar = { BottomNav(navController = navController)}
                    ) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}
