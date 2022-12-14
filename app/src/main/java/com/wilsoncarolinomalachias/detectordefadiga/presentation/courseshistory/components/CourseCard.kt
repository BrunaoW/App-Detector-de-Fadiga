package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CourseCardGreen(
    courseFinishDate: Date,
    courseStartAddress: String?,
    courseDestinationAddress: String?,
    modifier: Modifier = Modifier,
    onClickViewReport: () -> Unit = {}
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
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        border = BorderStroke(3.dp, greenColor),
        modifier = modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(text = "Corrida", fontSize = 20.sp)
            Text(text = "Finalizada em $courseFinishDateAsString", fontSize = 14.sp, color = greenColor)
            if (!courseStartAddress.isNullOrEmpty()) {
                Row {
                    Text(text = "De: ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = courseStartAddress, fontSize = 14.sp)
                }
            }
            if (!courseDestinationAddress.isNullOrEmpty()) {
                Row {
                    Text(text = "Para: ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = courseDestinationAddress, fontSize = 14.sp)
                }
            }

            Button(
                colors = mainButtonColor,
                shape = RoundedCornerShape(50),
                onClick = {
                    onClickViewReport()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            {
                Text(
                    text = "VISUALIZAR RELATÓRIO",
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
    courseStartAddress: String?,
    courseDestinationAddress: String?,
    modifier: Modifier = Modifier,
    onClickViewReport: () -> Unit = {}
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
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        border = BorderStroke(3.dp, orangeColor),
        modifier = modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Corrida - Fadiga detectada", fontSize = 20.sp)
            Text(text = "Finalizada em $courseFinishDateAsString", fontSize = 14.sp, color = orangeColor)
            if (!courseStartAddress.isNullOrEmpty()) {
                Row {
                    Text(text = "De: ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = courseStartAddress, fontSize = 14.sp)
                }
            }
            if (!courseDestinationAddress.isNullOrEmpty()) {
                Row {
                    Text(text = "Para: ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = courseDestinationAddress, fontSize = 14.sp)
                }
            }

            Button(
                colors = mainButtonColor,
                shape = RoundedCornerShape(50),
                onClick = {
                    onClickViewReport()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            {
                Text(
                    text = "VISUALIZAR RELATÓRIO",
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
            "Belo Vale = MG",
            modifier = Modifier
                .padding(horizontal = 16.dp)
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
