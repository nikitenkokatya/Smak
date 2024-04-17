package com.example.smak.compras.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smak.database.repository.ComprasRepository

class ComprasCreateViewModel :ViewModel() {
    private var state = MutableLiveData<ComprasCreateState>()

    val nombre = MutableLiveData<String>()

    fun getState(): MutableLiveData<ComprasCreateState> {
        return state
    }

    private val repository = ComprasRepository()

    fun agregarIngrediente(nombre: String) {
        repository.agregarIngrediente(nombre)
    }
}