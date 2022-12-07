package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.SearchAppBar
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.viewmodels.CourseHistoryViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme


@Composable
fun CoursesHistoryScreen(
    coursesHistoryViewModel: CourseHistoryViewModel = viewModel()
) {
    Column (
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchAppBar(
            text = "Busque por corridas feitas",
            onTextChange = {},
            onCloseClicked = {},
            onSearchClicked = {}
        )
    }
}
@Preview
@Composable
fun CoursesHistoryPreview() {
    DetectorDeFadigaTheme {
        CoursesHistoryScreen()
    }
}
