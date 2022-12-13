package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.CourseCardGreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.CourseCardOrange
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.SearchAppBar

@Composable
fun CourseReportScreen(
    navController: NavHostController
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

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
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                ReportCard(
                    courseStartAddress = "Rua João de Paula, Sagrada F. - Belo Horizonte",
                    courseDestinationAddress = "Belo Vale = MG",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}