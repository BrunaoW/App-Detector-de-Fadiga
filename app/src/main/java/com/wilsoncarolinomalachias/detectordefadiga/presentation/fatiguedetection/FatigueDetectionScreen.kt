package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.FaceBoundsOverlay
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels.FatigueDetectionViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels.IFatigueDetectionViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@ExperimentalGetImage
@Composable
fun FatigueDetectionScreen(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    fatigueDetectionViewModel: IFatigueDetectionViewModel = FatigueDetectionViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var timeSpentInSeconds by remember { mutableStateOf(0) }
    var timeSpentText by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }


    var fatigueDetectedCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            timeSpentInSeconds++
            val timeSpentMinutesText = ((timeSpentInSeconds % 3600) / 60).toString().padStart(2, '0')
            val timeSpentSecondsText = (timeSpentInSeconds % 60).toString().padStart(2, '0')
            timeSpentText = "$timeSpentMinutesText:$timeSpentSecondsText"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .border(2.dp, Color.Red),
            factory = { context ->
                val rootView = getRootView(context)
                val previewView = rootView.findViewById<PreviewView>(R.id.previewView).apply {
                    this.scaleType = scaleType
                }
                val faceBoundsOverlayView = rootView.findViewById<FaceBoundsOverlay>(R.id.faceBoundsOverlay)

                setupFatigueDetection(
                    fatigueDetectionViewModel = fatigueDetectionViewModel,
                    previewView = previewView,
                    rootView = rootView,
                    context = context,
                    faceBoundsOverlay = faceBoundsOverlayView,
                    lifecycleOwner = lifecycleOwner
                )

                return@AndroidView rootView
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tempo de corrida:")
            Text(text = timeSpentText)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Fadigas detectadas:")
            Text(text = "$fatigueDetectedCount")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { },
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(text = "Finalizar corrida")
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Preview
@Composable
fun FatigueDetectionScreenPreview() {
    DetectorDeFadigaTheme {
        FatigueDetectionScreen()
    }
}

private fun getRootView(context: Context): View {
    return LayoutInflater
        .from(context)
        .inflate(
            R.layout.face_detection_view,
            null,
            false
        ).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
}

private fun setupFatigueDetection(
    fatigueDetectionViewModel: IFatigueDetectionViewModel,
    previewView: PreviewView,
    rootView: View,
    context: Context,
    faceBoundsOverlay: FaceBoundsOverlay,
    lifecycleOwner: LifecycleOwner
) {
    fatigueDetectionViewModel.setPreviewUseCase(previewView.surfaceProvider)
    fatigueDetectionViewModel.setImageAnalysis(
        rootView.width,
        rootView.height
    )
    fatigueDetectionViewModel.setAnalyzer(context.executor) {
        faceBoundsOverlay.drawFaceBounds(it)
    }
    fatigueDetectionViewModel.setCameraProvider(lifecycleOwner, context)
}
