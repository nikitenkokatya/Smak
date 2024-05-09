package com.example.smak.perfil.ui.usecase

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Perfil
import com.example.smak.database.repository.PerfilRepository
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PerfilViewModel : ViewModel(){
    private val repository = PerfilRepository()

    fun getPerfil(userEmail: String, onSuccess:() -> Unit, onFailure:() -> Unit) {
        repository.getPerfil(userEmail, onSuccess, onFailure)
    }

    fun guardarPerfil(userName: String, base64: String) {
        viewModelScope.launch {
            repository.guardarPerfil(Perfil(userName, base64))
        }
    }
}