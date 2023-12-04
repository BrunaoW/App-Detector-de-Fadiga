package com.wilsoncarolinomalachias.detectordefadiga.presentation.translatedata.viewmodel

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.auth.oauth2.GoogleCredentials
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Serializable


class TranslateDataViewModel: ViewModel() {
    private val _currentImage: MutableStateFlow<ImageBitmap> = MutableStateFlow(
        ImageBitmap(500, 500, hasAlpha = true, config = ImageBitmapConfig.Argb8888)
    )
    val currentImage: StateFlow<ImageBitmap> = _currentImage.asStateFlow()

    private val _currentImageName: MutableStateFlow<String> = MutableStateFlow("")
    val currentImageName: StateFlow<String> = _currentImageName.asStateFlow()

    val fileListUrl = "https://www.googleapis.com/drive/v3/files?q="
    var authorizationToken: String? = null
    var googleDriveList: MutableList<GoogleDriveFile> = mutableListOf()

    val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()
    val detector = FaceDetection.getClient(realTimeOpts)

    val facesList: MutableList<CustomFace> = mutableListOf()

    var nextPageToken: String? = null

    fun obterTokenDeAcesso(context: Context) {
        val assetManager: AssetManager = context.assets
        val fileInputStream = assetManager.open("fadigalert-7a0e2585c44e.json")

        val credentials = GoogleCredentials
            .fromStream(fileInputStream)
            .createScoped(
                listOf(
                    "https://www.googleapis.com/auth/drive",
                )
            )

        viewModelScope.launch(Dispatchers.IO) {
            val accessToken = credentials.refreshAccessToken()

            authorizationToken = accessToken.tokenValue
        }
    }

    fun getFilesListFromGoogleDrive(folderId: String) {
        if (authorizationToken.isNullOrEmpty()) {
            return
        }
        googleDriveList.clear()

        viewModelScope.launch(Dispatchers.IO) {
            var i = 1
            do {
                Log.d(TAG, "Listando arquivos ${i * 100} de 5000 (d) ou 12000(nd)")

                var listFilesUrl = "$fileListUrl'$folderId' in parents&pageSize=100"
                if (nextPageToken != null) {
                    listFilesUrl += "&pageToken=" + nextPageToken;
                }

                val request = Request.Builder()
                    .url(listFilesUrl)
                    .header("Authorization", "Bearer $authorizationToken")
                    .build()

                val client = OkHttpClient()
                client.newCall(request).execute().also {response ->
                    if (!response.isSuccessful)
                        return@also

                    val responseBody = response.body?.string() ?: return@also

                    val parsedJson = Json.parseToJsonElement(responseBody).jsonObject
                    val filesInfoList = parsedJson["files"]!!.jsonArray

                    val files = filesInfoList.mapNotNull{
                        val jsonFile = it.jsonObject
                        if (jsonFile["mimeType"]!!.jsonPrimitive.content != "image/jpeg") {
                            return@mapNotNull null
                        }

                        GoogleDriveFile(
                            jsonFile["kind"]!!.jsonPrimitive.content,
                            jsonFile["mimeType"]!!.jsonPrimitive.content,
                            jsonFile["id"]!!.jsonPrimitive.content,
                            jsonFile["name"]!!.jsonPrimitive.content
                        )
                    }

                    googleDriveList.addAll(files)
                    nextPageToken = parsedJson["nextPageToken"]?.jsonPrimitive?.content
                    i++
                }
            } while(nextPageToken != null && (i * 100) <= 100)
        }
    }

    suspend fun downloadAndProcessImage(fileName: String, fileId: String) = viewModelScope.launch(Dispatchers.IO) {
        val fileDownloadUrl = "https://www.googleapis.com/drive/v3/files/$fileId?alt=media"


        val request = Request.Builder()
            .url(fileDownloadUrl)
            .header("Authorization", "Bearer $authorizationToken")
            .build()

        val client = OkHttpClient()
        client.newCall(request).execute().also {response ->
            if (!response.isSuccessful)
                return@also

            val inputStream = response.body?.byteStream()

            if (inputStream != null) {
                val imageFile = File.createTempFile("image", ".jpg")
                try {
                    val outputStream = FileOutputStream(imageFile)
                    inputStream.use { input ->
                        outputStream.use { fileOut ->
                            input.copyTo(fileOut)
                        }
                    }
                    // Chamar a função de processamento para a imagem baixada
                    processImage(fileName, imageFile) {
                        outputStream.close()
                        inputStream.close()
                        imageFile.delete()
                    }
                } catch (e: Exception) {
                    inputStream.close()
                    imageFile.delete()
                }
            }
        }
    }

    private suspend fun processImage(fileName: String, imageFile: File, onFinishProcessing: () -> Boolean) {
        val bitmapFile = BitmapFactory.decodeFile(imageFile.absolutePath)

        _currentImage.value = bitmapFile.asImageBitmap()

        val inputImage = InputImage.fromBitmap(bitmapFile, 0)

        val facesList: List<Face> = detector.process(inputImage).await()
        val face = facesList.firstOrNull()

        if (face != null) {
            saveFaceInPointsList(fileName, face)
        }

        onFinishProcessing()
    }

    private fun saveFaceInPointsList(fileName: String, face: Face) {
        facesList.add(CustomFace(fileName, face))
    }

    fun translateFileListToFaceDetection(isDrowsy: Boolean) {
        var currentJob: Job
        var i = 0

        for (file in googleDriveList) {
            if (i > 20)
                break

            try {
                runBlocking {
                    Log.d(TAG, "Arquivo ${file.name} - $i de ${googleDriveList.size}")

                    currentJob = downloadAndProcessImage(file.name, file.id)
                    currentJob.join()
                }
            } catch (e: Exception) {
                continue
            } finally {
                i++
                _currentImageName.value = "Imagem $i de ${googleDriveList.size}"
            }
        }

        createJsonFileAndUploadToGoogleDrive(isDrowsy)
    }

    private fun createJsonFileAndUploadToGoogleDrive(isDrowsy: Boolean) {
        val gson = Gson()
        val json = gson.toJson(facesList)
        val folderToSaveId = "1QvjzjzH8mba7bLLBuQkyVK85K7xZyHiI"
        val jsonFileName = "dataset_facesList" + if (isDrowsy) "_drowsy" else "_notdrowsy"

        // Escrever o JSON para um arquivo
        val inputStream = json.byteInputStream()
        val jsonFile = File.createTempFile(jsonFileName, ".json")
        val outputStream = FileOutputStream(jsonFile)
        inputStream.use { input ->
            outputStream.use { fileOut ->
                input.copyTo(fileOut)
            }
        }

        val uploadUrl = "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart"
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "metadata",
                null,
                RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    "{\"name\": \"$jsonFileName.json\", \"parents\": [\"$folderToSaveId\"]}"
                )
            )
            .addFormDataPart(
                "file",
                jsonFile.name,
                RequestBody.create("application/json".toMediaTypeOrNull(), jsonFile)
            )
            .build()

        val request = Request.Builder()
            .header("Authorization", "Bearer $authorizationToken")
            .url(uploadUrl)
            .post(requestBody)
            .build()

        // Execução da requisição
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Lidar com falha na requisição
            }

            override fun onResponse(call: Call, response: Response) {
                // Lidar com a resposta da requisição
                val responseBody = response.body?.string() ?: return
                outputStream.close()
                inputStream.close()
            }
        })
    }

    companion object {
        const val TAG: String = "TranslateDataViewModel"
    }
}

data class GoogleDriveFile(
    val kind: String,
    val mimeType: String,
    val id: String,
    val name: String
): Serializable

class CustomFace(
    fileName: String,
    face: Face
) {
    val fileName: String
    val allPoints: List<PointF>
    val leftEyePerClos: Float
    val rightEyePerClos: Float

    init {
        this.fileName = fileName
        this.allPoints = face.allContours.fold(listOf()) { contours, contour ->
            contours + contour.points
        }

        leftEyePerClos = face.leftEyeOpenProbability ?: 0f
        rightEyePerClos = face.rightEyeOpenProbability ?: 0f
    }
}
