package com.example.smak.compras.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Compras
import com.example.smak.database.repository.ComprasRepository
import kotlinx.coroutines.launch

class ComprasListViewModel : ViewModel() {
    private var ingredienteBorrado : Compras? = null
    private val comprasRepository = ComprasRepository()
    private val comprasList = MutableLiveData<List<Compras>>()
    private val state = MutableLiveData<ListStateCompras>()

    fun getState(): LiveData<ListStateCompras> {
        return state
    }

    fun getComprasList(): LiveData<List<Compras>> {
        return comprasList
    }

    fun obtenerIngredientes() {
        comprasRepository.getList { listaIngredientes ->
            comprasList.postValue(listaIngredientes)

            when {
                listaIngredientes.isEmpty() -> state.postValue(ListStateCompras.NoData)
                else -> state.postValue(ListStateCompras.Success)
            }
        }
    }

    fun borrarIngrediente(compras: Compras) {
        viewModelScope.launch {
            try {
                comprasRepository.borrarIngrediente(compras)
                ingredienteBorrado = compras
                obtenerIngredientes()
            } catch (e: Exception) {
                Log.e("ComprasListViewModel", "Error al borrar el ingrediente: ${e.message}")
            }
        }
    }

    fun deshacerBorrado() {
        viewModelScope.launch {
            try {
                comprasRepository.agregarIngrediente(ingredienteBorrado!!.nombre)
                ingredienteBorrado = null
                obtenerIngredientes()
            } catch (e: Exception) {
                Log.e("ComprasListViewModel", "Error al añadir el ingrediente: ${e.message}")
            }
        }
    }
}