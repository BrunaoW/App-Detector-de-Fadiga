package com.wilsoncarolinomalachias.detectordefadiga.presentation.coursereport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    hasFatigue: Boolean,
    courseEvents: ArrayList<Pair<Long, String>>,
    modifier: Modifier = Modifier
) {
    val brownColor = Color(0xFF734D15)
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Corrida ${if (hasFatigue) "(Fadiga detectada)" else ""}",
                    fontSize = 20.sp,
                    color = if (hasFatigue) brownColor else Color.Blue
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
        }

        items(courseEvents) { event ->
            val eventDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            ).format(event.first)

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("$eventDate - ", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(event.second, fontSize = 14.sp)
            }
        }

        item {
            Spacer(modifier = Modifier.height(128.dp))
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
            hasFatigue = true,
            arrayListOf()
        )
    }
}
