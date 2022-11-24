package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.CourseCardGreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.CourseCardOrange
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components.SearchAppBar
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun CoursesHistoryScreen() {
    Column (verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
        SearchAppBar(text = "Busque por corridas feitas", onTextChange = {}, onCloseClicked = {}, onSearchClicked = {})
        CourseCardGreen()
        CourseCardOrange()
        CourseCardOrange()
    }
}

@Preview
@Composable
fun CourseCardPreview() {
    DetectorDeFadigaTheme {
        CoursesHistoryScreen()
    }
}