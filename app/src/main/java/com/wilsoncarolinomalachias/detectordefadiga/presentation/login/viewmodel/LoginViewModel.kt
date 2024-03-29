package com.wilsoncarolinomalachias.detectordefadiga.presentation.login.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.wilsoncarolinomalachias.detectordefadiga.presentation.AppAuthentication
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Credentials

class LoginViewModel: ViewModel() {

    fun checkCredentials(creds: Credentials, context: Context): Boolean {
        if (creds.isNotEmpty() && creds.login == "admin") {

            return true
        } else {
            Toast.makeText(context, "Dados inválidos", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun login(credentials: Credentials, context: Context, onSuccessAction: () -> Unit) {
        val isCredentialsNotValid = credentials.login.isEmpty()
            ||  credentials.pwd.isEmpty()

        if (isCredentialsNotValid)
            return

        AppAuthentication.loginUserWithEmailAndPassword(
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