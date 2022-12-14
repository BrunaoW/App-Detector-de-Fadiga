package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels

import android.content.Context
import android.graphics.PointF
import android.location.Address
import android.location.Geocoder
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.getCameraProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class FatigueDetectionViewModel : ViewModel() {

    var fatigueDetectedCount: Int = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val addressAndTimeList: MutableList<Pair<Long, String>> = mutableListOf()

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

    var isCurrentlyFaceNotFound = mutableStateOf(false)
        private set

    private val faceNotFoundTimer = object : CountDownTimer(10_000, 10_000) {
        var isCountingTime: Boolean = false

        override fun onTick(millisUntilFinished: Long) { }

        override fun onFinish() {
            ringtoneManager.play()
            isCurrentlyFaceNotFound.value = true
        }
    }

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
        var isRunningAlarm: Boolean = false

        override fun onTick(millisUntilFinished: Long) { }

        override fun onFinish() {
            isRunningAlarm = false
            yawnAndTimeOccured.clear()
            ringtoneManager.stop()
        }
    }

    private var isYawnOccuring: Boolean = false
    private val yawnAndTimeOccured: MutableList<Long> = mutableListOf()

    private fun notifyFatigue() {
        fatigueAlarmTimer.isRunningAlarm = true
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

    fun initFatigueDetectionRoutine(context: Context, processImageCallback: (List<PointF>) -> Unit) {
        initLocationClient(context)
        initAddressesSavesInListWhileExecuting(context)
        initAnalyzer(context, processImageCallback)
    }

    private fun initLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private fun initAnalyzer(context: Context, processImageCallback: (List<PointF>) -> Unit) {
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
                        val face = faces.firstOrNull()

                        if (face == null) {
                            if (!faceNotFoundTimer.isCountingTime) {
                                faceNotFoundTimer.start()
                                faceNotFoundTimer.isCountingTime = true
                            }

                            processImageCallback(listOf())
                        } else {
                            faceNotFoundTimer.cancel()
                            faceNotFoundTimer.isCountingTime = false

                            val allPoints = face.allContours.fold(listOf<PointF>()) { contours, contour ->
                                contours + contour.points
                            }

                            processImageCallback(allPoints)
                            processFaceFeaturesAndDetectFatigue(face, routineTimeStart)
                        }

                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }

    fun resetIsFaceNotFound() {
        isCurrentlyFaceNotFound.value = false
        ringtoneManager.stop()
    }

    private fun processFaceFeaturesAndDetectFatigue(face: Face, routineTimeStart: Date) {
        detectEyesClosedForTooLong(face)
        detectMultipleYawnsInShortSpan(face)
    }

    private fun detectMultipleYawnsInShortSpan(face: Face) {
        // Detectar se boca abriu e ficou por pelo menos 2 segundos
        val upperLipBottom = face.getContour(FaceContour.UPPER_LIP_BOTTOM)
        val lowerLipTop = face.getContour(FaceContour.LOWER_LIP_TOP)

        val uppperLipBottomPositionSum = upperLipBottom?.points?.fold(0f) { acc, point ->
            return@fold acc + point.x + point.y
        } ?: 0f

        val lowerLipTopPositionSum = lowerLipTop?.points?.fold(0f) { acc, point ->
            return@fold acc + point.x + point.y
        } ?: 0f

        val sumPercentDiff = uppperLipBottomPositionSum / lowerLipTopPositionSum
        val actualTime = Date()

        // Fazer uma contagem de bocejos:
        // Caso tenha bocejado 2 vezes em um tempo de 1 minuto, enviar evento de fadiga
        if (sumPercentDiff < 0.99 && !isYawnOccuring) {
            isYawnOccuring = true
            yawnAndTimeOccured.add(actualTime.time)
        } else if (sumPercentDiff >= 0.99) {
            isYawnOccuring = false
        }

        if (isYawnOccuring)
            return

        val yawnsHappenedInLastMinute = yawnAndTimeOccured.count {
            (it - actualTime.time) < 60
        }

        if (yawnsHappenedInLastMinute > 2 && !fatigueAlarmTimer.isRunningAlarm) {
            notifyFatigue()
        }
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

    fun generateCourse(): Course {
        val startAddress = addressAndTimeList.firstOrNull()?.second
        val finalAddress = addressAndTimeList.lastOrNull()?.second ?: startAddress

        val placesVisited = ArrayList(addressAndTimeList.map { it.second })

        val course = Course(
            uid = 0,
            startDate = Date(),
            finishDate = Date(),
            startAddress = startAddress,
            destinationAddress = finalAddress,
            placesVisited = placesVisited,
            fatigueCount = fatigueDetectedCount
        )

        return course
    }

    private fun initAddressesSavesInListWhileExecuting(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val currentLocation = it.result
                    val currentAddress = Geocoder(context)
                        .getFromLocation(
                            currentLocation.latitude,
                            currentLocation.longitude,
                            1
                        ).firstOrNull() ?: return@addOnCompleteListener

                    val addressString = "${currentAddress.thoroughfare}, ${currentAddress.subLocality}, ${currentAddress.subAdminArea} - ${currentAddress.adminArea}"

                    addressAndTimeList.add(Pair(Date().time, addressString))
                }

                delay(60_000)
            }
        }
    }
}
