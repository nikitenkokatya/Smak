package com.example.smak.ui.usecase

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Receta
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.repository.RecetaRepository.Companion.agregarReceta
import com.example.smak.database.resource.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Base64

class CreateViewModel :ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    private var state = MutableLiveData<CreateState>()

    val nombre = MutableLiveData<String>()
    val ingredientes = MutableLiveData<String>()
    val pasos = MutableLiveData<String>()
    val tiempo = MutableLiveData<String>()
    lateinit var tipo: String
    private val _imagenes = MutableLiveData<List<String>>().apply { value = mutableListOf() }
    val imagenes: LiveData<List<String>> = _imagenes
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
                imagenes.value.isNullOrEmpty() -> state.value = CreateState.ImagenesEmptyError

                else->{
                    val result = repository.agregarReceta(Receta(nombre.value!!, ingredientes.value!!, pasos.value!!,tiempo.value!!, tipo, "",
                        (imagenes.value ?: mutableListOf()).toMutableList()
                    ))
                    when(result){
                        is Resource.Error -> state.value = CreateState.Error(result.ex)
                        is Resource.Success<*>->state.value = CreateState.Success(result.data as Receta)
                    }
                }
            }
        }
    }
    fun addImage(base64: String) {
        val currentImages = _imagenes.value?.toMutableList() ?: mutableListOf()
        currentImages.add(base64)
        _imagenes.value = currentImages
    }

    /*fun addPhotoToRecipe(uri: Uri) {
        val currentRecipe = Receta(nombre.value!!, ingredientes.value!!, pasos.value!!, tiempo.value!!, tipo, "",  imagenes as MutableList<Uri>)
        currentRecipe.imagenes.add(uri)
        agregarReceta(currentRecipe)
    }*/
}