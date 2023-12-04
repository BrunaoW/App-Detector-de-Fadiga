package com.wilsoncarolinomalachias.detectordefadiga.presentation.signUp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.AppAuthentication
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Credentials

class SignUpViewModel : ViewModel() {
    fun signUp(credentials: Credentials, context: Context, onSuccessAction: () -> Unit) {
        if (AppAuthentication.isUserLoggedIn()) {
            return
        }

        val isCredentialsNotValid = credentials.login.isEmpty()
            ||  credentials.pwd.isEmpty()

        if (isCredentialsNotValid)
            return

        AppAuthentication.createUserAuthentication(
            credentials.login,
            credentials.pwd,
            onSuccessAction = {
                onSuccessAction()
            },
            onFailAction = {
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        )
    }
}