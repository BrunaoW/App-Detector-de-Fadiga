package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.BorderStroke
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CourseCardGreen(
    courseFinishDate: Date,
    courseStartAddress: String,
    courseDestinationAddress: String
) {
    val courseFinishDateAsString =  SimpleDateFormat(
        "yyyy-MM-dd HH:mm",
        Locale.getDefault()
    ).format(courseFinishDate)

    val greenColor = Color(0xFF1CC900)

    val mainButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = greenColor,
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(3.dp, greenColor),
        modifier = Modifier.padding(10.dp)
    ) {

        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)){
            Text(text = "Corrida", fontSize = 20.sp)
            Text(text = "Finalizada em $courseFinishDateAsString", fontSize = 14.sp, color = greenColor)
            Text(text = "De: $courseStartAddress", fontSize = 14.sp)
            Text(text = "Para: $courseDestinationAddress", fontSize = 14.sp)

            Button(
                colors = mainButtonColor,
                shape = RoundedCornerShape(50),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )
            {
                Text(
                    text = "GERAR RELATÓRIO",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun CourseCardOrange(
    courseFinishDate: Date,
    courseStartAddress: String,
    courseDestinationAddress: String
) {
    val courseFinishDateAsString =  SimpleDateFormat(
        "yyyy-MM-dd HH:mm",
        Locale.getDefault()
    ).format(courseFinishDate)

    val orangeColor = Color(0xFFFF9500)

    val mainButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = orangeColor,
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(3.dp, orangeColor),
        modifier = Modifier.padding(10.dp)
    ) {

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Corrida - Fadiga detectada", fontSize = 20.sp)
            Text(text = "Finalizada em $courseFinishDateAsString", fontSize = 14.sp, color = orangeColor)
            Text(text = "De: $courseStartAddress", fontSize = 14.sp)
            Text(text = "Para: $courseDestinationAddress", fontSize = 14.sp)

            Button(
                colors = mainButtonColor,
                shape = RoundedCornerShape(50),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )
            {
                Text(
                    text = "GERAR RELATÓRIO",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun OrangeCourseCardPreview() {
    DetectorDeFadigaTheme {
        CourseCardOrange(
            Date(),
            "Rua João de Paula, Sagrada F. - Belo Horizonte",
            "Belo Vale = MG"
        )
    }
}

@Preview
@Composable
fun GreenCourseCardPreview() {
    DetectorDeFadigaTheme {
        CourseCardGreen(
            Date(),
            "Rua João de Paula, Sagrada F. - Belo Horizonte",
            "Belo Vale = MG"
        )
    }
}
