package com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.card.MaterialCardView
import com.wilsoncarolinomalachias.detectordefadiga.BuildConfig
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.components.StartCourseButton
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun StartCourseScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StartCourseButton {
            navController.navigate(Screen.FatigueDetection.route)
        }
//        Button(onClick = {
//            navController.navigate(Screen.TranslateData.route)
//        }) {
//            Text(text = "Executar rotina de tradução de dados")
//        }
    }
}

@Preview
@Composable
fun StartCourseScreenPreview() {
    val navController = rememberNavController()
    DetectorDeFadigaTheme {
        StartCourseScreen(navController = navController)
    }
}