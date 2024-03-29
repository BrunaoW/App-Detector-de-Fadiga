package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.BottomNav
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.primaryColor

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Color.White
            )

            val context = LocalContext.current

            DetectorDeFadigaTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            BottomNav(navController = navController)
                        },
                        content = {
                            NavigationGraph(
                                navController = navController,
                                context = context,
                                modifier = Modifier.padding(it)
                            )
                        }
                    )
                }
            }
        }

        checkCameraPermission()
        checkLocationPermission()
        checkFineLocationPermission()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                    "Permissão de câmera não aceita.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_PERMISSIONS
        )

        return false
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_CODE_PERMISSIONS
        )

        return false
    }
    private fun checkFineLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_PERMISSIONS
        )

        return false
    }

    fun checkStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(baseContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSIONS
        )

        return false
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
