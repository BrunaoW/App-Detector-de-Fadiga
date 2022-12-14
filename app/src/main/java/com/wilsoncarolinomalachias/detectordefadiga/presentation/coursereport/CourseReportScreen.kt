package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport.viewmodels.CourseReportViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import com.wilsoncarolinomalachias.detectordefadiga.presentation.viewmodels.CourseViewModel
import kotlinx.coroutines.launch

@Composable
fun CourseReportScreen(
    navController: NavHostController,
    courseId: String,
    courseReportViewModel: CourseReportViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val courseIdAsInt = courseId.toIntOrNull()
    val courseViewModel = CourseViewModel(context)

    if (courseIdAsInt == null) {
        Toast
            .makeText(context, "Id de curso inválido", Toast.LENGTH_LONG)
            .show()
        navController.navigate(Screen.StartCourseScreen.route)
        return
    }

    val loadedCourse = courseReportViewModel.loadedCourse.value

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val course = courseViewModel.getCourse(courseIdAsInt).first()
            courseReportViewModel.setLoadedCourse(course)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Relatórios")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                backgroundColor = Color(0xFF001CBB),
                contentColor = Color.White
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* ... */ },
                icon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Salvar"
                    )
                },
                text = { Text("SALVAR EM PDF") },
                backgroundColor = Color.Blue,
                contentColor = Color.White,
            )
        },

        content = {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                ReportCard(
                    courseStartAddress = loadedCourse?.startAddress.orEmpty(),
                    courseDestinationAddress = loadedCourse?.destinationAddress.orEmpty(),
                    courseEvents = loadedCourse?.eventsWithTimeList ?: arrayListOf(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview
@Composable
fun CoursesReportPreview() {
    val navController = rememberNavController()
    DetectorDeFadigaTheme {
        CourseReportScreen(
            navController = navController,
            courseId = "courseId"
        )
    }
}
