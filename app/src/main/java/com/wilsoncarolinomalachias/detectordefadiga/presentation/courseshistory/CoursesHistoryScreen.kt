package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import java.util.*


@Composable
fun CoursesHistoryScreen() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Relatórios")
                },
                navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.ArrowBack, "backIcon")
                }
                },
                backgroundColor = Color(0xFF001CBB),
                contentColor = Color.White,

            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column (
                Modifier
                .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                SearchAppBar(
                    text = "Busque por corridas feitas",
                    onTextChange = {},
                    onCloseClicked = {},
                    onSearchClicked = {}
                )
                CourseCardGreen(
                    Date(),
                    "Rua João de Paula, Sagrada F. - Belo Horizonte",
                    "Belo Vale - MG")
                CourseCardOrange(
                    Date(),
                    "Rua Aristóteles Ribeiro Vasconcelos. - Belo Horizonte",
                    "Itapecerica - MG")
                CourseCardOrange(
                    Date(),
                    "Avenida Antônio Carlos 6676. - Belo Horizonte",
                    "Belo Vale - MG"
                )
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
