package com.wilsoncarolinomalachias.detectordefadiga.presentation

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AppAuthentication {
    private var auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun getUserName(): String {
        return auth.currentUser?.displayName ?: return "Não logado"
    }

    fun createUserAuthentication(
        email: String,
        pwd: String,
        onSuccessAction: (FirebaseUser) -> Unit = {},
        onFailAction: () -> Unit = {}
    ) {
        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AppAuthentication", "createUserWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        onSuccessAction(user)
                    } else {
                        onFailAction()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AppAuthentication", "createUserWithEmail:failure", task.exception)
                    onFailAction()
                }
            }
    }

    fun loginUserWithEmailAndPassword(
        email: String,
        pwd: String,
        onSuccessAction: (FirebaseUser) -> Unit = {},
        onFailAction: () -> Unit = {}
    ) {
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("AppAuthentication", "loginUserWithEmailAndPassword:success")
                val user = auth.currentUser
                if (user != null) {
                    onSuccessAction(user)
                } else {
                    onFailAction()
                }
            } else {
                Log.w("AppAuthentication", "createUserWithEmail:failure", task.exception)
                onFailAction()
            }
        }
    }
}