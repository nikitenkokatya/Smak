package com.example.smak.compras.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.smak.compras.data.Compras
import com.example.smak.database.repository.ComprasRepository
import com.example.smak.ui.usecase.ListState
import kotlinx.coroutines.launch

class ComprasListViewModel :ViewModel(){
    private val comprasRepository = ComprasRepository()
    private val comprasList = MutableLiveData<List<Compras>>()
    private val state = MutableLiveData<ListState>()

    fun getState(): LiveData<ListState> {
        return state
    }

    fun getComprasList(): LiveData<List<Compras>> {
        return comprasList
    }

    fun obtenerIngredientes() {
        //state.postValue(ListState.Loading)
        comprasRepository.getList { listaIngredientes ->
            comprasList.postValue(listaIngredientes)
            state.postValue(ListState.Success)
        }
    }

  fun borrarIngrediente(compras: Compras) {
        viewModelScope.launch {
            try {
                comprasRepository.borrarIngrediente(compras)
                obtenerIngredientes()
            } catch (e: Exception) {
                Log.e("ComprasListViewModel", "Error al borrar el ingrediente: ${e.message}")
                state.postValue(ListState.Error)
            }
        }
    }

}