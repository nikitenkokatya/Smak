package com.example.smak

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.resource.Resource
import com.example.smak.ui.usecase.ListState
import kotlinx.coroutines.launch

class CreadasListViewModel : ViewModel() {
    private val repository = RecetaRepository()
    private val recetas = MutableLiveData<List<Receta>>()
    fun getRecetas(): MutableLiveData<List<Receta>> {
        return recetas
    }

    private var state = MutableLiveData<ListState>()
    fun getState(): MutableLiveData<ListState> {
        return state
    }

    fun getMisRecetas() {
        viewModelScope.launch {
            try {
                repository.getMisRecetas() { list ->
                    when {
                        list.isEmpty() -> state.postValue(ListState.Error)
                        else -> {
                            recetas.postValue(list)
                            state.postValue(ListState.Success)
                        }
                    }
                }
            } catch (e: Exception) {
                state.postValue(ListState.Error)
            }
        }
    }
}