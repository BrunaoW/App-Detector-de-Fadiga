package com.wilsoncarolinomalachias.detectordefadiga.presentation.login.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
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
}