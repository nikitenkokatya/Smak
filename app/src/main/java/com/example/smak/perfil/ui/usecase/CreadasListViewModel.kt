package com.example.smak.perfil.ui.usecase

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.ui.usecase.ListState
import kotlinx.coroutines.launch

class CreadasListViewModel : ViewModel() {
    private val repository = RecetaRepository()
    private val recetas = MutableLiveData<List<Receta>>()
    fun getRecetas(): MutableLiveData<List<Receta>> {
        return recetas
    }

    private var state = MutableLiveData<CreadasListState>()
    fun getState(): MutableLiveData<CreadasListState> {
        return state
    }

    fun getMisRecetas() {
        viewModelScope.launch {
            try {
                repository.getMisRecetas() { list ->
                    when {
                        list.isEmpty() -> state.postValue(CreadasListState.Error)
                        else -> {
                            recetas.postValue(list)
                            state.postValue(CreadasListState.Success)
                        }
                    }
                }
            } catch (e: Exception) {
                state.postValue(CreadasListState.Error)
            }
        }
    }

    fun getMisRecetas(textView: TextView) {
        viewModelScope.launch {
            try {
                repository.getMisRecetas() { list ->
                    when {
                        list.isEmpty() -> {
                            textView.text = "0"
                            state.postValue(CreadasListState.Error)
                        }

                        else -> {
                            recetas.postValue(list)
                            textView.text = list.size.toString()
                            state.postValue(CreadasListState.Success)
                        }
                    }
                }
            } catch (e: Exception) {
                state.postValue(CreadasListState.Error)
            }
        }
    }

    fun borrarReceta(receta: Receta) {
        viewModelScope.launch {
            try {
                repository.borrarMiReceta(receta)

            } catch (e: Exception) {
                Log.e("ComprasListViewModel", "Error al borrar el ingrediente: ${e.message}")
            }
        }
    }
}