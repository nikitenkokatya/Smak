package com.example.smak.comentarios.usecase

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository

class PersonaViewModel : ViewModel() {
    private val recetaRepository = RecetaRepository()

    private val _recetasPrivadas = MutableLiveData<List<Receta>>()
    val recetasPrivadas: LiveData<List<Receta>> get() = _recetasPrivadas

    fun obtenerRecetasPrivadas(email: String, textView: TextView) {
        recetaRepository.getRecetasPrivadas(email) { recetas ->
            _recetasPrivadas.value = recetas
            textView.text = recetas.size.toString()

        }
    }
}
