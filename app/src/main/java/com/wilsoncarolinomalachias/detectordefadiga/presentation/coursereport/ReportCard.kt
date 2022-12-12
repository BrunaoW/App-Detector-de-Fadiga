package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@Composable
fun ReportCard(
    courseStartAddress: String,
    courseDestinationAddress: String
) {

    val brownColor = Color(0xFF734D15)

    val mainButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = brownColor,
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 10.dp,
            modifier = Modifier.padding(10.dp)
        ) {

            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)){
                Text(text = "Corrida (Fadiga detectada)", fontSize = 20.sp, color = brownColor)
                Text(text = "De: $courseStartAddress", fontSize = 14.sp)
                Text(text = "Para: $courseDestinationAddress", fontSize = 14.sp)

                Row() {
                    Icon(Icons.Rounded.LocationOn, contentDescription = "Localized description", tint = Color.Blue)
                    Text(text = "De: $courseStartAddress", fontSize = 14.sp)
                }
            }
        }
    }

}

@Preview
@Composable
fun GreenCourseCardPreview() {
    DetectorDeFadigaTheme {
        ReportCard(
            "Rua João de Paula, Sagrada F. - Belo Horizonte",
            "Belo Vale = MG"
        )
    }
}
