package com.wilsoncarolinomalachias.detectordefadiga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.ui.theme.DetectorDeFadigaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetectorDeFadigaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colors.background
                ) {
                    TelaInicial()
                }
            }
        }
    }
}

@Composable
fun TelaInicial() {
    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier
            .background(Color.Blue, shape = CircleShape)
            .size(200.dp)
    ) {
        Text(
            text = "Iniciar corrida",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DetectorDeFadigaTheme {
        TelaInicial()
    }
}