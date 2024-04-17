package com.example.smak.ui.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.resource.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
                val result = RecetaRepository.getAllRecetas()
                when (result) {
                    is Resource.Success<*> -> {
                        val recetas = result.data as List<Receta>
                        // Aquí puedes realizar cualquier acción con la lista de recetas obtenida
                        // Por ejemplo, puedes actualizar un LiveData para que la UI lo observe
                         _recetas.postValue(recetas)
                        state.postValue(ListState.Success)
                    }
                    is Resource.Error -> {
                        // Manejar el caso de error
                        state.postValue(ListState.Error)
                    }
                }
            } catch (e: Exception) {
                // Manejar el caso de excepción
                state.postValue(ListState.Error)
            }
        }
    }
}