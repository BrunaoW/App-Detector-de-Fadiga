package com.wilsoncarolinomalachias.detectordefadiga.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.LoginForm
import com.wilsoncarolinomalachias.detectordefadiga.presentation.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(128.dp),
            )
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {
                Text("Não é cadastrado? ")
                ClickableText(text = AnnotatedString("Cadastre-se aqui"), onClick = {
                    navController.navigate(Screen.Signup.route)
                })
            }
            LoginForm(
                buttonText = "Entrar",
                buttonAction = { credentials ->
                    loginViewModel.login(credentials, context) {
                        navController.navigate(Screen.StartCourse.route)
                    }
                }
            )

        }
    }
}