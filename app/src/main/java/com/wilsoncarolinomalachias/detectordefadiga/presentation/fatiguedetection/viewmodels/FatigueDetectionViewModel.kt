package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels

import android.content.Context
import android.graphics.PointF
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
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
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.getCameraProvider
import kotlinx.coroutines.launch
import java.util.*

class FatigueDetectionViewModel : ViewModel() {

    var fatigueDetectedCount: Int = 0

    val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(realTimeOpts)

    private lateinit var ringtoneManager: Ringtone

    lateinit var previewUseCase: Preview
    lateinit var imageAnalysis: ImageAnalysis

    private var leftEyeClosedProbability: Float = 0f
    private var rightEyeClosedProbability: Float = 0f

    private var isEyesCurrentlyClosed: Boolean = false
    private val closedEyesTimer = object : CountDownTimer(2000, 2000) {
        var isCountingTime: Boolean = false

        override fun onTick(millisUntilFinished: Long) { }

        override fun onFinish() {
            isCountingTime = false

            if (isEyesCurrentlyClosed) {
                notifyFatigue()
            }
        }
    }

    private val fatigueAlarmTimer = object : CountDownTimer(4000, 500) {
        override fun onTick(millisUntilFinished: Long) { }

        override fun onFinish() {
            ringtoneManager.stop()
        }
    }

    private var yawnCount: Int = 0

    private fun notifyFatigue() {
        ringtoneManager.play()
        fatigueAlarmTimer.start()
        fatigueDetectedCount += 1
    }

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
    
    fun initAnalyzer(context: Context, processImageCallback: (List<PointF>) -> Unit) {
        val executor = context.executor
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtoneManager = RingtoneManager.getRingtone(context, ringtoneUri)

        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image
            val routineTimeStart = Date()

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
                        face?.let {
                            processFaceFeaturesAndDetectFatigue(it, routineTimeStart)
                        }

                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }

    private fun processFaceFeaturesAndDetectFatigue(face: Face, routineTimeStart: Date) {
        detectEyesClosedForTooLong(face)
        detectMultipleYawnsInShortSpan(face, routineTimeStart)
        detectFocusDeviationFromTheRoad(face)
    }

    private fun detectFocusDeviationFromTheRoad(face: Face) {
        // Salvar o "foco médio" do motorista (rotação da face em relação à câmera)

        // Checar se o desvio do foco está maior de uma dada porcentagem/angulo durante um tempo longo
        // Exemplo: 30%/40º por 4-5 segundos

        // Se sim, enviar evento de fadiga
    }

    private fun detectMultipleYawnsInShortSpan(face: Face, routineTimeStart: Date) {
        // Fazer uma contagem de bocejos


        // Caso tenha bocejado 2 vezes em um tempo de 1 minuto, enviar evento de fadiga
    }

    private fun detectEyesClosedForTooLong(face: Face) {
        // usar o dado "eyeClosedProbability"
        rightEyeClosedProbability = 1 - (face.rightEyeOpenProbability ?: 1f)
        leftEyeClosedProbability = 1 - (face.leftEyeOpenProbability ?: 1f)

        // Se ele ficou abaixo de uma dada porcentagem (90%) durante 2 segundos, enviar evento de fadiga
        val isEyesClosedInLastTick = isEyesCurrentlyClosed
        isEyesCurrentlyClosed = rightEyeClosedProbability > 0.9 && leftEyeClosedProbability > 0.9

        if (isEyesClosedInLastTick) {
            if (isEyesCurrentlyClosed) {
                if (closedEyesTimer.isCountingTime)
                    return

                closedEyesTimer.start()
                closedEyesTimer.isCountingTime = true
            } else {
                closedEyesTimer.cancel()
                closedEyesTimer.isCountingTime = false
            }
        }
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
