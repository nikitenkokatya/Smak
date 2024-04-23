package com.example.smak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smak.data.Receta
import com.example.smak.database.repository.GuardadasRepository

class GuardadasViewModel:ViewModel() {
    private val guardadasRepository = GuardadasRepository()

    private val _recetasFavoritas = MutableLiveData<Set<Receta>>()
    val recetasFavoritas: LiveData<Set<Receta>> = _recetasFavoritas

    fun cargarRecetasFavoritas() {
        guardadasRepository.getRecetaFavorita(
            onSuccess = {
                _recetasFavoritas.value = guardadasRepository.recetasFavoritas
            }
        )
    }

    fun agregarRecetaFavorita(receta: Receta) {
        guardadasRepository.addRecetaFavorita(receta)
    }
}