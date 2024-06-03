package com.example.smak.ui.usecase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.resource.Resource
import kotlinx.coroutines.launch

class ListViewModel : ViewModel(){

    private val repository = RecetaRepository()
    private val _recetas = MutableLiveData<List<Receta>>()
    val recetas = _recetas

    private var state = MutableLiveData<ListState>()
    fun getState():MutableLiveData<ListState>{
        return state
    }

    fun getAllRecetas() {
        viewModelScope.launch {
            try {
                state.value = ListState.Loading(true)
                val result = RecetaRepository.getAllRecetas()
                state.value = ListState.Loading(false)

                when (result) {
                    is Resource.Success<*> -> {
                        val recetas = result.data as List<Receta>
                         _recetas.postValue(recetas)
                        state.postValue(ListState.Success)
                    }
                    is Resource.Error -> {
                        state.postValue(ListState.Error)
                    }
                }
            } catch (e: Exception) {
                state.postValue(ListState.Error)
            }
        }
    }

    /*fun getAllRecetasTipo(tipo: String) {
        viewModelScope.launch {
            try {
                state.value = ListState.Loading(true)
                val result = RecetaRepository.getAllRecetas()
                state.value = ListState.Loading(false)

                when (result) {
                    is Resource.Success<*> -> {
                        val recetas = result.data as List<Receta>
                        // Filtrar las recetas por tipo
                        val recetasFiltradas = if (tipo.isNotEmpty()) {
                            recetas.filter { it.tipo.equals(tipo, ignoreCase = true) }
                        } else {
                            recetas
                        }
                        _recetas.postValue(recetasFiltradas)
                        state.postValue(ListState.Success)
                    }
                    is Resource.Error -> {
                        state.postValue(ListState.Error)
                    }
                }
            } catch (e: Exception) {
                state.postValue(ListState.Error)
            }
        }
    }*/

    fun getAllRecetasTipo(tipo: String, query: String) {
        viewModelScope.launch {
            try {
                state.value = ListState.Loading(true)
                val result = RecetaRepository.getAllRecetas()
                state.value = ListState.Loading(false)

                when (result) {
                    is Resource.Success<*> -> {
                        val recetas = result.data as List<Receta>
                        // Filtrar las recetas por tipo y por la consulta de búsqueda
                        val recetasFiltradas = recetas.filter {
                            it.tipo.equals(tipo, ignoreCase = true) && it.title.contains(query, ignoreCase = true)
                        }
                        _recetas.postValue(recetasFiltradas)
                        state.postValue(ListState.Success)
                    }
                    is Resource.Error -> {
                        state.postValue(ListState.Error)
                    }
                }
            } catch (e: Exception) {
                state.postValue(ListState.Error)
            }
        }
    }

}