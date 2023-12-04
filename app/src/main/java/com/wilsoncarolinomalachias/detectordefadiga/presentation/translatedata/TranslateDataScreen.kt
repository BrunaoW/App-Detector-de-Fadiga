package com.wilsoncarolinomalachias.detectordefadiga.presentation.translatedata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.translatedata.viewmodel.TranslateDataViewModel

@Composable
fun TranslateDataScreen(
    navController: NavHostController,
    translateDataViewModel: TranslateDataViewModel = viewModel()
) {
    val currentImage by translateDataViewModel.currentImage.collectAsState()
    val currentImageName by translateDataViewModel.currentImageName.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit){
        translateDataViewModel.obterTokenDeAcesso(context)
    }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { translateDataViewModel.getFilesListFromGoogleDrive("1QyNGQnZjH8aDquRON1i_krWMw23oWgaj") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Obter Lista de arquivos - Drowsy")
        }
        Button(
            onClick = { translateDataViewModel.translateFileListToFaceDetection(true) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Baixar e traduzir arquivos - Drowsy")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { translateDataViewModel.getFilesListFromGoogleDrive("1lrTVlw2gEaS9VnsebbON5tOx-fo0_1v7") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        ) {
            Text(text = "Obter Lista de arquivos - Not drowsy")
        }
        Button(
            onClick = { translateDataViewModel.translateFileListToFaceDetection(false) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        ) {
            Text(text = "Baixar e traduzir arquivos - Not drowsy")
        }
        Image(
            bitmap = currentImage,
            contentDescription = "Imagem atual",
            modifier = Modifier.fillMaxSize()
        )
        Text(text = currentImageName)
    }
}