package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels

import android.content.Context
import android.graphics.PointF
import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executor

interface IFatigueDetectionViewModel {
    fun setPreviewUseCase(surfaceProvider: Preview.SurfaceProvider)
    fun setImageAnalysis(dimensionX: Int, dimensionY: Int)
    fun setAnalyzer(executor: Executor, processImageCallback: (List<PointF>) -> Unit)
    fun setCameraProvider(lifecycleOwner: LifecycleOwner, context: Context)
}
