package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun CourseCardGreen() {
    val paddingModifier = Modifier.padding(10.dp)
    val mainButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = Color.Green,

    )
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(3.dp, Color.Green),
        modifier = paddingModifier
    ) {

        Column(modifier = paddingModifier){
            Text(text = "Corrida", fontSize = 20.sp)
            Text(text = "Finalizada em 03/10/2022 às 19:30", fontSize = 14.sp, color = Color.Green)
            Text(text = "De: Rua João de Paula, Sagrada F. - Belo Horizonte", fontSize = 14.sp)
            Text(text = "Para: Belo Vale - MG", fontSize = 14.sp)

            Button(colors = mainButtonColor, shape = RoundedCornerShape(50), onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)

            )
            {
                Text(text = "GERAR RELATÓRIO", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun CourseCardPreview() {
    DetectorDeFadigaTheme {
        CourseCardGreen()
    }
}
