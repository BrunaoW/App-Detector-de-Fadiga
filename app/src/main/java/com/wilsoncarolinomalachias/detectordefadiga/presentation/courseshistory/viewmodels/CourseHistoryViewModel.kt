package com.wilsoncarolinomalachias.detectordefadiga.presentation.courseshistory.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wilsoncarolinomalachias.detectordefadiga.presentation.entities.Course

class CourseHistoryViewModel : ViewModel() {
    // Acessa sharedPreferences e obter dados de viagens feitas
    private lateinit var sharedPreferences: SharedPreferences

    val coursesMutableStateList = mutableStateListOf<Course>()

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(COURSE_HISTORY_SP, Context.MODE_PRIVATE)
    }

    fun getCourses() {
        val gson = Gson()

        // Obter lista de ids de corridas feitas
        val courseIdList = sharedPreferences.getStringSet(COURSE_HISTORY_LIST_ID, setOf())
            ?: setOf()

        // pra cada id da lista, carregar todas as corridas feitas e salvar em uma variavel
        val coursesList = courseIdList.mapNotNull { cId ->
            val courseJson = sharedPreferences.getString("${COURSE_HISTORY_INFO}_$cId", "")
            val course = gson.fromJson(courseJson, Course::class.java)
            return@mapNotNull course
        }

        // Enviar como evento a lista de corridas carregadas, pra atualizar na pagina
        coursesMutableStateList.clear()
        coursesMutableStateList.addAll(coursesList)
    }

    companion object {
        const val COURSE_HISTORY_SP = "course_history_sp"
        const val COURSE_HISTORY_LIST_ID = "course_history_list_id"
        const val COURSE_HISTORY_INFO = "course_history_info"
    }
}