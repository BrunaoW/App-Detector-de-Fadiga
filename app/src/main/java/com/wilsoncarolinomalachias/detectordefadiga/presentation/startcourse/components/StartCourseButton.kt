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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun StartCourseButton(
    onClickStartCourse: () -> Unit
) {
    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier
            .shadow(
                6.dp,
                shape = CircleShape
            )
            .background(Color.Blue, shape = CircleShape)
            .size(200.dp)
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

@Preview
@Composable
fun StartCourseButtonPreview() {
    DetectorDeFadigaTheme {
        StartCourseButton {

        }
    }
}