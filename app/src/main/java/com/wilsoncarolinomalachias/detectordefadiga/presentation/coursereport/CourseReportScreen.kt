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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import com.wilsoncarolinomalachias.detectordefadiga.presentation.viewmodels.CourseViewModel

@Composable
fun CourseReportScreen(
    navController: NavHostController,
    courseId: String
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val context = LocalContext.current

    val courseIdAsInt = courseId.toIntOrNull()
    val courseViewModel = CourseViewModel(context)

    if (courseIdAsInt == null) {
        Toast
            .makeText(context, "Id de corrida inválido", Toast.LENGTH_LONG)
            .show()
        navController.popBackStack()
    }

    val coursesList = courseViewModel.coursesLiveData.observeAsState()

//    if (loadedCourse == null) {
//        Toast
//            .makeText(context, "Corrida inválida", Toast.LENGTH_LONG)
//            .show()
//        navController.popBackStack()
//    }

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
                backgroundColor = Color.Blue,
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
            val course = coursesList.value?.firstOrNull { it.uid == courseIdAsInt }

            ReportCard(
                courseStartAddress = course?.startAddress.orEmpty(),
                courseDestinationAddress = course?.destinationAddress.orEmpty(),
                hasFatigue = (course?.fatigueCount ?: 0) > 0,
                courseEvents = course?.eventsWithTimeList ?: arrayListOf(),
                modifier = Modifier.fillMaxWidth()
            )
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
