package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class CourseHistoryViewModel : ViewModel() {
    // Acessa sharedPreferences e obter dados de viagens feitas
    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(COURSE_HISTORY_SP, Context.MODE_PRIVATE)
    }

    fun getCourses() {
        // Obter lista de ids de corridas feitas

        // pra cada id da lista, carregar todas as corridas feitas e salvar em uma variavel

        // Enviar como evento a lista de corridas carregadas, pra atualizar na pagina
    }

    companion object {
        const val COURSE_HISTORY_SP = "course_history_sp"
        const val COURSE_HISTORY_LIST_ID = "course_history_list_id"
    }
}