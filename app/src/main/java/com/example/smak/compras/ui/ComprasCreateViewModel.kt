package com.example.smak.compras.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smak.database.repository.ComprasRepository

class ComprasCreateViewModel :ViewModel() {
    private var state = MutableLiveData<ComprasCreateState>()
    private val repository = ComprasRepository()
    val nombre = MutableLiveData<String>()

    fun getState(): MutableLiveData<ComprasCreateState> {
        return state
    }

    fun agregarIngrediente(nombre: String) {
        repository.agregarIngrediente(nombre)
    }
}