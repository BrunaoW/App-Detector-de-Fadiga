package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.FaceBoundsOverlay
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels.FatigueDetectionViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme

@ExperimentalGetImage
@Composable
fun FatigueDetectionScreen(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    fatigueDetectionViewModel: FatigueDetectionViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier
                .padding(24.dp)
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
        Row {
            Text(text = "Tempo de corrida:", modifier = Modifier.fillMaxWidth())
            Text(text = "00:00")
        }
        Row {
            Text(text = "Fadigas detectadas:")
            Text(text = "00")
        }
        Button(onClick = { /*TODO*/ }) {

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
                ViewGroup.LayoutParams.MATCH_PARENT
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
    fatigueDetectionViewModel.setAnalyzer(context.executor) {
        faceBoundsOverlay.drawFaceBounds(it)
    }
    fatigueDetectionViewModel.setCameraProvider(lifecycleOwner, context)
}
