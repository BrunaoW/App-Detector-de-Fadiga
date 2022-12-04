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
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.getCameraProvider
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class FatigueDetectionViewModel : ViewModel() {

    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(realTimeOpts)

    lateinit var previewUseCase: Preview
    lateinit var imageAnalysis: ImageAnalysis

    fun setPreviewUseCase(surfaceProvider: SurfaceProvider) {
        previewUseCase = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(surfaceProvider)
            }
    }

    fun setImageAnalysis(dimensionX: Int, dimensionY: Int) {
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(dimensionX, dimensionY))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    
    fun setAnalyzer(executor: Executor, processImageCallback: (List<PointF>) -> Unit) {
        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image

            if (image != null) {
                val processImage = InputImage.fromMediaImage(image, rotationDegrees)

                detector
                    .process(processImage)
                    .addOnSuccessListener { faces ->
                        val face: Face? = faces.firstOrNull()

                        val allPoints = face?.allContours?.fold(listOf<PointF>()) { contours, contour ->
                            contours + contour.points
                        } ?: listOf()

                        processImageCallback(allPoints)
                        face?.let { processFaceFeaturesAndDetectFatigue(it) }

                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }

    private fun processFaceFeaturesAndDetectFatigue(face: Face) {
        detectEyesClosedForTooLong(face)
        detectMultipleYawnsInShortSpan(face)
        detectFocusDeviationFromTheRoad(face)

        face.rightEyeOpenProbability
        face.leftEyeOpenProbability
    }

    private fun detectFocusDeviationFromTheRoad(face: Face) {
        // Salvar o "foco médio" do motorista (rotação da face em relação à câmera)

        // Checar se o desvio do foco está maior de uma dada porcentagem/angulo durante um tempo longo
        // Exemplo: 30%/40º por 4-5 segundos

        // Se sim, enviar evento de fadiga
    }

    private fun detectMultipleYawnsInShortSpan(face: Face) {
        // Fazer uma contagem de bocejos
        // Caso tenha bocejado 2 vezes em um tempo de 1 minuto, enviar evento de fadiga
    }

    private fun detectEyesClosedForTooLong(face: Face) {
        // usar o dado "eyeClosedProbability"

        // Se ele ficou abaixo de uma dada porcentagem (90%) durante 2 segundos, enviar evento de fadiga
    }

    fun setCameraProvider(lifecycleOwner: LifecycleOwner, context: Context) {
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

    fun updateImageViewResolution(width: Int, height: Int) {
        if (imageAnalysis.camera == null) {
            return
        }

        imageAnalysis.updateSuggestedResolution(
            Size(width, height)
        )
    }

}
