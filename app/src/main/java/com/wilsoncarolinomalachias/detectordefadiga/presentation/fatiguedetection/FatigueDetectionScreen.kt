package com.wilsoncarolinomalachias.detectordefadiga.presentation.fatiguedetection

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.FaceBoundsOverlay
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalGetImage
@Composable
fun FatigueDetectionScreen(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

//    High-accuracy landmark detection and face classification
//    val highAccuracyOpts = FaceDetectorOptions.Builder()
//        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//        .build()

    // Real-time contour detection
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(realTimeOpts)

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

                // CameraX Preview UseCase
                val previewUseCase = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                var face: Face? = null

                imageAnalysis.setAnalyzer(
                    context.executor
                ) { imageProxy ->
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val image = imageProxy.image

                    if (image != null) {
                        val processImage = InputImage.fromMediaImage(image, rotationDegrees)

                        detector
                            .process(processImage)
                            .addOnSuccessListener { faces ->
                                face = faces.firstOrNull()
                                face?.let { faceBoundsOverlay.drawFaceBounds(it.allLandmarks) }

//                                for (face in faces) {
//                                    faceBounds = face.boundingBox
//                                    faceRotationY = face.headEulerAngleY // Head is rotated to the right rotY degrees
//                                    faceRotationZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees
//
//                                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                                    // nose available):
//                                    val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
//                                    leftEar?.let {
//                                        val leftEarPos = leftEar.position
//                                    }
//
//                                    // If contour detection was enabled:
//                                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
//                                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points
//
//                                    // If classification was enabled:
//                                    if (face.smilingProbability != null) {
//                                        val smileProb = face.smilingProbability
//                                    }
//                                    if (face.rightEyeOpenProbability != null) {
//                                        val rightEyeOpenProb = face.rightEyeOpenProbability
//                                    }
//
//                                    // If face tracking was enabled:
//                                    if (face.trackingId != null) {
//                                        val id = face.trackingId
//                                    }
//                                }
                                imageProxy.close()
                            }
                            .addOnFailureListener {
                                imageProxy.close()
                            }

                    }
                }

                coroutineScope.launch {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        // Must unbind the use-cases before rebinding them.
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, previewUseCase
                        )
                    } catch (ex: Exception) {
                        Log.e("CameraPreview", "Use case binding failed", ex)
                    }
                }

                return@AndroidView rootView
            }
        )
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            executor
        )
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)