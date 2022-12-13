package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.viewmodels.CourseHistoryViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import com.wilsoncarolinomalachias.detectordefadiga.presentation.viewmodels.CourseViewModel


@Composable
fun CoursesHistoryScreen(
    navController: NavHostController
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val context = LocalContext.current

    val courseViewModel = CourseViewModel(context)

    val coursesList = courseViewModel.coursesLiveData.observeAsState()

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
        content = {
            LazyColumn (
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                item {
                    SearchAppBar(
                        text = "Busque por corridas feitas",
                        onTextChange = {},
                        onCloseClicked = {},
                        onSearchClicked = {}
                    )
                }

                items(coursesList) { course ->
                    if (course.fatigueCount > 0) {
                        CourseCardOrange(
                            courseFinishDate = course.finishDate,
                            courseStartAddress = course.startAddress,
                            courseDestinationAddress = course.destinationAddress
                        )
                    } else {
                        CourseCardGreen(
                            courseFinishDate = course.finishDate,
                            courseStartAddress = course.startAddress,
                            courseDestinationAddress = course.destinationAddress
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun CoursesHistoryPreview() {
    val navController = rememberNavController()
    DetectorDeFadigaTheme {
        CoursesHistoryScreen(navController = navController)
    }
}
