package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme


@Composable
fun CoursesHistoryScreen() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = {Text("Top App Bar")},backgroundColor = MaterialTheme.colors.primary)  },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column (verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
                SearchAppBar(text = "Busque por corridas feitas", onTextChange = {}, onCloseClicked = {}, onSearchClicked = {})
                CourseCardGreen()
                CourseCardOrange()
                CourseCardOrange()
            }
        },
    )
}
@Preview
@Composable
fun CoursesHistoryPreview() {
    DetectorDeFadigaTheme {
        CoursesHistoryScreen()
    }
}
