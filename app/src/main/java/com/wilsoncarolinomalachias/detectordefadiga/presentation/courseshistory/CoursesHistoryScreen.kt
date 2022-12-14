package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
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
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                item {
                    SearchAppBar(
                        text = "Busque por corridas feitas",
                        onTextChange = {},
                        onCloseClicked = {},
                        onSearchClicked = {},
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp)
                    )
                }

                items(coursesList.value.orEmpty()) { course ->
                    if (course.fatigueCount > 0) {
                        CourseCardOrange(
                            courseFinishDate = course.finishDate,
                            courseStartAddress = course.startAddress,
                            courseDestinationAddress = course.destinationAddress,
                            onClickViewReport = {
                                navController.navigate(
                                    "${Screen.CourseReport.route}/${course.uid}"
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    } else {
                        CourseCardGreen(
                            courseFinishDate = course.finishDate,
                            courseStartAddress = course.startAddress,
                            courseDestinationAddress = course.destinationAddress,
                            onClickViewReport = {
                                navController.navigate(
                                    "${Screen.CourseReport.route}/${course.uid}"
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
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
