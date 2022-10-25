package com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.components.StartCourseButton

@Composable
fun StartCourseScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StartCourseButton()
    }
}