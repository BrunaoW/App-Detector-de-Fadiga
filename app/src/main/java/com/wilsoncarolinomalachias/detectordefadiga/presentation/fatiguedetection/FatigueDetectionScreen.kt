package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.FaceBoundsOverlay
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels.FatigueDetectionViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import com.wilsoncarolinomalachias.detectordefadiga.presentation.viewmodels.CourseViewModel
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
    fatigueDetectionViewModel: FatigueDetectionViewModel = viewModel(),
    navController: NavHostController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var timeSpentInSeconds by remember { mutableStateOf(0) }
    var timeSpentText by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }

    val context = LocalContext.current

    val courseViewModel = CourseViewModel(context)

    val isFaceNotFound = fatigueDetectionViewModel.isCurrentlyFaceNotFound

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
        if (isFaceNotFound.value) {
            AlertDialog(
                onDismissRequest = {
                    fatigueDetectionViewModel.resetIsFaceNotFound()
                },
                title = { Text(text = "Falha na captura do rosto") },
                text = { Text("Não foi possível capturar o rosto do motorista. Verificar o motivo com urgência após parar o carro.") },
                confirmButton = { },
                dismissButton = {
                    Button(
                        onClick = {
                            fatigueDetectionViewModel.resetIsFaceNotFound()
                        }
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }

        AndroidView(
            modifier = Modifier
                .border(2.dp, Color.Red),
            factory = {
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
        ) {
            val faceBoundsOverlayView = it.findViewById<FaceBoundsOverlay>(R.id.faceBoundsOverlay)
            fatigueDetectionViewModel.updateImageViewResolution(
                faceBoundsOverlayView.width,
                faceBoundsOverlayView.height
            )
        }
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
            Text(text = "${fatigueDetectionViewModel.fatigueDetectedCount}")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { },
            onClick = {
                fatigueDetectionViewModel.generateCourse(context) { generatedCourse ->
                    courseViewModel.addCourse(generatedCourse)

                    val navOptions = NavOptions
                        .Builder()
                        .setPopUpTo(Screen.StartCourseScreen.route, false)
                        .build()

                    navController.navigate(
                        "${Screen.CourseReport.route}/${generatedCourse.uid}",
                        navOptions,
                        null
                    )
                }
            },
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
    val navController = rememberNavController()

    DetectorDeFadigaTheme {
        FatigueDetectionScreen(navController = navController)
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
    fatigueDetectionViewModel: FatigueDetectionViewModel,
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
    fatigueDetectionViewModel.initFatigueDetectionRoutine(context) {
        faceBoundsOverlay.drawFaceBounds(it)
    }
    fatigueDetectionViewModel.setCameraProvider(lifecycleOwner, context)
}
