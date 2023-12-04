package com.wilsoncarolinomalachias.detectordefadiga.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Credentials

@Composable
fun LoginForm(
    buttonAction: (Credentials) -> Unit = {},
    buttonText: String
) {
    val credentials by remember { mutableStateOf(Credentials()) }
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        LoginField(
            placeholder = "E-mail",
            onChange = {
                credentials.login = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        PasswordField(
            placeholder = "Senha",
            onChange = {
                credentials.pwd = it
            },
            submit = { },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { buttonAction(credentials) },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonText)
        }
    }
}