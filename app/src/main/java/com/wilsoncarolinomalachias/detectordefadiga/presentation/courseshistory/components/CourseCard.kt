package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun CourseCard() {
    val paddingModifier = Modifier.padding(10.dp)
    Card(shape = RoundedCornerShape(20.dp),elevation = 10.dp,modifier = Modifier
        .background(Color.White)

    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(text = "Corrida")
            Text(text = "Finalizada em 03/10/2022 às 19:30")
            Text(text = "De: Rua João de Paula, Sagrada F. - Belo Horizonte")
            Text(text = "Para: Belo Vale - MG")

            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth(0.8f)
            )
            {}
        }
    }
}

@Preview
@Composable
fun CourseCardPreview() {
    DetectorDeFadigaTheme {
        CourseCard()
    }
}
