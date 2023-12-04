package com.wilsoncarolinomalachias.detectordefadiga.presentation.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wilsoncarolinomalachias.detectordefadiga.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.wilsoncarolinomalachias.detectordefadiga.presentation.Screen
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.LoginForm
import com.wilsoncarolinomalachias.detectordefadiga.presentation.signUp.viewmodel.SignUpViewModel


@Composable
fun SignUpScreen(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel = viewModel()
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
            Text("Cadastro", modifier = Modifier.padding(top = 16.dp))
            LoginForm(
                buttonText = "Cadastrar",
                buttonAction = { credentials ->
                    signUpViewModel.signUp(credentials, context) {
                        navController.navigate(Screen.StartCourse.route)
                    }
                }
            )
        }
    }
}