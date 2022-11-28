package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.CoursesHistoryScreen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme


@Composable
fun ScaffoldSample() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = {Text("Top App Bar")},backgroundColor = MaterialTheme.colors.primary)  },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {}){
            Icon(imageVector = Icons.Default.Add, contentDescription = "fab icon")
        } },
        drawerContent = { Text(text = "Drawer Menu 1") },
        content = { Text("Content") },
    )
}
@Preview
@Composable
fun ToolbarWidgetPreview() {
    DetectorDeFadigaTheme {
        ScaffoldSample()
    }
}
