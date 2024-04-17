package com.example.smak.ui.usecase

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.resource.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CreateViewModel :ViewModel(){

    private val db = FirebaseFirestore.getInstance()

    private var state = MutableLiveData<CreateState>()

    val nombre = MutableLiveData<String>()
    val ingredientes = MutableLiveData<String>()
    val pasos = MutableLiveData<String>()
    val tiempo = MutableLiveData<String>()
    lateinit var tipo: String
    val repository = RecetaRepository

    fun getState(): MutableLiveData<CreateState> {
        return state
    }

    fun validateCredentials(){
        viewModelScope.launch {
            when{
                TextUtils.isEmpty(nombre.value) -> state.value = CreateState.NombreEmptyError
                TextUtils.isEmpty(ingredientes.value)->state.value = CreateState.IngredientesEmptyError
                TextUtils.isEmpty(pasos.value) -> state.value = CreateState.PasosError
                TextUtils.isEmpty(tiempo.value)->state.value = CreateState.TiempoError
                else->{
                    val result = repository.agregarReceta(Receta(nombre.value!!, ingredientes.value!!, pasos.value!!,tiempo.value!!, tipo, ""))
                    when(result){
                        is Resource.Error -> state.value = CreateState.Error(result.ex)
                        is Resource.Success<*>->state.value = CreateState.Success(result.data as Receta)
                    }
                }
            }
        }
    }
}