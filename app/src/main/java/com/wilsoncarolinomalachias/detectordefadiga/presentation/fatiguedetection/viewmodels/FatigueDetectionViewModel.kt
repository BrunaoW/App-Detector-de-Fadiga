package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.getCameraProvider
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class FatigueDetectionViewModel : ViewModel(), IFatigueDetectionViewModel {

    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(realTimeOpts)

    lateinit var previewUseCase: Preview
    lateinit var imageAnalysis: ImageAnalysis

    override fun setPreviewUseCase(surfaceProvider: SurfaceProvider) {
        previewUseCase = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(surfaceProvider)
            }
    }

    override fun setImageAnalysis(dimensionX: Int, dimensionY: Int) {
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(dimensionX, dimensionY))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    
    override fun setAnalyzer(executor: Executor, processImageCallback: (List<PointF>) -> Unit) {
        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image

            if (image != null) {
                var face: Face?
                val processImage = InputImage.fromMediaImage(image, rotationDegrees)

                detector
                    .process(processImage)
                    .addOnSuccessListener { faces ->
                        face = faces.firstOrNull()
                        val allPoints = face?.allContours?.fold(listOf<PointF>()) { contours, contour ->
                            contours + contour.points
                        } ?: listOf()

                        processImageCallback(allPoints)

                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }
    
    override fun setCameraProvider(lifecycleOwner: LifecycleOwner, context: Context) {
        viewModelScope.launch { 
            try {
                val cameraProvider = context.getCameraProvider()
                // Must unbind the use-cases before rebinding them.
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, imageAnalysis, previewUseCase
                )
            } catch (ex: Exception) {
                Log.e("CameraPreview", "Use case binding failed", ex)
            }
        }
    }

}
