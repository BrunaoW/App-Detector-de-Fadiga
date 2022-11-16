package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.FaceBoundsOverlay
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels.FatigueDetectionViewModel

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
            modifier = modifier
                .padding(24.dp)
                .border(2.dp, Color.Red),
            factory = { context ->
                val rootView = LayoutInflater.from(context).inflate(R.layout.face_detection_view, null, false).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val previewView = rootView.findViewById<PreviewView>(R.id.previewView).apply {
                    this.scaleType = scaleType
                }

                val faceBoundsOverlay = rootView.findViewById<FaceBoundsOverlay>(R.id.faceBoundsOverlay)

                fatigueDetectionViewModel.setPreviewUseCase(previewView.surfaceProvider)
                fatigueDetectionViewModel.setImageAnalysis(
                    rootView.width,
                    rootView.height
                )
                fatigueDetectionViewModel.setAnalyzer(context.executor) {
                    faceBoundsOverlay.drawFaceBounds(it)
                }
                fatigueDetectionViewModel.setCameraProvider(lifecycleOwner, context)

                return@AndroidView rootView
            }
        )
    }
}
