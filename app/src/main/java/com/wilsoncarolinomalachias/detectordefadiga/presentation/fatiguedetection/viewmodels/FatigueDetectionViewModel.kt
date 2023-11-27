package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Geocoder
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.executor
import com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection.utils.getCameraProvider
import com.wilsoncarolinomalachias.detectordefadiga.presentation.translatedata.viewmodel.CustomFace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class FatigueDetectionViewModel : ViewModel() {

    private lateinit var trainedModelInterpreter: Interpreter
    private var fatigueProbability: Float = 0f

    var fatigueDetectedCount: Int = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var startAddress: String = ""
    private var finalAddress: String = ""
    private val eventWithTimeList: MutableList<Pair<Long, String>> = mutableListOf()

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

    var isCurrentlyFaceNotFound = mutableStateOf(false)
        private set

    private val fatigueOccuringTimer = object : CountDownTimer(5_000, 1_000) {
        var tempoExcedido = false

        override fun onTick(millisUntilFinished: Long) {
            if (fatigueProbability > 0.8) {
                tempoExcedido = true
            } else {
                tempoExcedido = false
                cancel() // Se a probabilidade for menor, interrompe o timer
            }
        }

        override fun onFinish() {
            notifyFatigue()
        }
    }

    private val faceNotFoundTimer = object : CountDownTimer(10_000, 10_000) {
        var isCountingTime: Boolean = false

        override fun onTick(millisUntilFinished: Long) { }

        override fun onFinish() {
            isCurrentlyFaceNotFound.value = true
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

    private val yawnAndTimeOccured: MutableList<Long> = mutableListOf()

    private fun notifyFatigue() {
        fatigueAlarmTimer.isRunningAlarm = true
        ringtoneManager.play()
        fatigueAlarmTimer.start()
        fatigueDetectedCount += 1

        val fatigueTime = Date()
        eventWithTimeList.add(Pair(fatigueTime.time, "Fadiga detectada"))
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
        initAddressesSavesInListRoutine(context)
        initAnalyzer(context, processImageCallback)
    }

    private fun initLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initAnalyzer(context: Context, processImageCallback: (List<PointF>) -> Unit) {
        val executor = context.executor
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtoneManager = RingtoneManager.getRingtone(context, ringtoneUri)

        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image

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
                            processFaceFeaturesAndDetectFatigue(face)
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

    private fun processFaceFeaturesAndDetectFatigue(face: Face) {
        if (!this::trainedModelInterpreter.isInitialized)
            return

        val customFace = CustomFace("", face)
        val allFacePoints = customFace.allPoints

        if (allFacePoints.isEmpty())
            return

        val qtdPoints = allFacePoints.size
        val classificationInputData = FloatArray(268) {index ->
            // Calculo o indice pro ponto atual a fim de não exceder os limites do vetor que tem
            // um tamanho menor que `qtdPoints`
            val currentPoint = allFacePoints[index % (qtdPoints - 1)]

            if (index < qtdPoints)
                return@FloatArray currentPoint.x
            else if (index < qtdPoints * 2)
                return@FloatArray currentPoint.y
            else if (index == 266)
                return@FloatArray customFace.leftEyePerClos
            else
                return@FloatArray customFace.rightEyePerClos
        }

        val classificationInputBuffer = ByteBuffer
            .allocateDirect(classificationInputData.size * 4)
            .order(ByteOrder.nativeOrder())
        classificationInputBuffer.rewind()
        classificationInputBuffer.asFloatBuffer().put(classificationInputData)

        val outputTensorSize = trainedModelInterpreter
            .getOutputTensor(0).numBytes()

        val outputBuffer = ByteBuffer
            .allocateDirect(outputTensorSize)
            .order(ByteOrder.nativeOrder())

        trainedModelInterpreter.run(classificationInputBuffer, outputBuffer)

        val fatigueProbability = outputBuffer.getFloat(0)
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

    @SuppressLint("RestrictedApi")
    fun updateImageViewResolution(width: Int, height: Int) {
        if (imageAnalysis.camera == null) {
            return
        }

        imageAnalysis.updateSuggestedResolution(
            Size(width, height)
        )
    }

    fun generateCourse(context: Context, onCompletion: (course: Course) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            val currentLocation = it.result
            val currentAddress = Geocoder(context)
                .getFromLocation(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    1
                )?.firstOrNull() ?: return@addOnCompleteListener

            finalAddress = "${currentAddress.thoroughfare}, ${currentAddress.subLocality}, ${currentAddress.subAdminArea} - ${currentAddress.adminArea}"

            val course = Course(
                uid = 0,
                startDate = Date(),
                finishDate = Date(),
                startAddress = startAddress,
                destinationAddress = finalAddress,
                eventsWithTimeList = ArrayList(eventWithTimeList),
                fatigueCount = fatigueDetectedCount.toInt()
            )

            onCompletion(course)
        }
    }

    private fun initAddressesSavesInListRoutine(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            var isFirstExecution = true

            while(true) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@launch
                }
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val currentLocation = it.result
                    val currentAddress = Geocoder(context)
                        .getFromLocation(
                            currentLocation.latitude,
                            currentLocation.longitude,
                            1
                        )?.firstOrNull() ?: return@addOnCompleteListener

                    val addressString = "${currentAddress.thoroughfare}, ${currentAddress.subLocality}, ${currentAddress.subAdminArea} - ${currentAddress.adminArea}"

                    eventWithTimeList.add(Pair(Date().time, addressString))

                    if (isFirstExecution) {
                        startAddress = addressString
                        isFirstExecution = false
                    }
                }

                delay(60_000)
            }
        }
    }

    fun initializeTFLite(context: Context) {
        val options = TfLiteInitializationOptions.builder()
            .setEnableGpuDelegateSupport(true)
            .build()

        val tfliteModel = loadModelFileFromAssets(context, "model.tflite")

        TfLiteVision.initialize(context, options).addOnSuccessListener {
            trainedModelInterpreter = Interpreter(tfliteModel)
        }.addOnFailureListener {
            TfLiteVision.initialize(context).addOnSuccessListener {
                trainedModelInterpreter = Interpreter(tfliteModel)
            }.addOnFailureListener{
                Toast.makeText(context, "O detector TFLite falhou", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadModelFileFromAssets(context: Context, modelName: String): ByteBuffer {
        val assetManager = context.assets
        val inputStream = assetManager.open(modelName)
        val fileSize = inputStream.available()
        val buffer = ByteBuffer.allocateDirect(fileSize)

        inputStream.use { input ->
            buffer.apply {
                val bytes = ByteArray(buffer.capacity())
                input.read(bytes)
                buffer.put(bytes)
                buffer.rewind() // Volta para o início do buffer
            }
        }

        return buffer
    }
}
