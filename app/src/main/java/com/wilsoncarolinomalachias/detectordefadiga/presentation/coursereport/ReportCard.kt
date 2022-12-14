package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun ReportCard(
    courseStartAddress: String,
    courseDestinationAddress: String,
    courseEvents: ArrayList<Pair<Long, String>>,
    modifier: Modifier = Modifier
) {

    val brownColor = Color(0xFF734D15)
    
    Card(
        shape = RoundedCornerShape(0.dp),
        elevation = 2.dp,
        modifier = modifier
    ) {

        ConstraintLayout(
            modifier = Modifier.padding(10.dp)
        ) {
            val (headerRef, timelineRef, addressListRef) = createRefs()

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(headerRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                Text(
                    text = "Corrida (Fadiga detectada)",
                    fontSize = 20.sp,
                    color = brownColor
                )

                Text(
                    text = "De: $courseStartAddress",
                    fontSize = 14.sp
                )

                Text(
                    text = "Para: $courseDestinationAddress",
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier
                    .constrainAs(timelineRef) {
                        top.linkTo(headerRef.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, 10.dp)
                        height = Dimension.fillToConstraints
                    }
                    .background(color = Color.Blue)
                    .width(2.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .constrainAs(addressListRef) {
                    top.linkTo(headerRef.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(timelineRef.start,10.dp)
                },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(courseEvents) { event ->
                    val (indicatorRef, addressRef) = createRefs()
                    val eventDate = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                    ).format(event.first)

                    Text(
                        "$eventDate - ${event.second}",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .constrainAs(addressRef) {
                                start.linkTo(indicatorRef.end,10.dp)
                            }
                    )
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
            "Belo Vale = MG",
            arrayListOf()
        )
    }
}
