package com.wilsoncarolinomalachias.detectordefadiga.presentation.startcourse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StartCourseButton(
    onClickStartCourse: () -> Unit
) {
    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier
            .background(Color.Blue, shape = CircleShape)
            .size(200.dp)
            .shadow(
                2.dp,
                shape = CircleShape
            )
            .clickable {
                onClickStartCourse()
            }
    ) {
        Text(
            text = "Iniciar corrida",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}