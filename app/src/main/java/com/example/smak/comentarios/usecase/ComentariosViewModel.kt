package com.example.smak.comentarios.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smak.data.Comentario
import com.example.smak.database.repository.ComentarioRepository
import com.example.smak.database.repository.PerfilRepository
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ComentariosViewModel : ViewModel() {

    private val comentarioRepository = ComentarioRepository()

    private val _comentarios = MutableLiveData<List<Comentario>>()
    val comentarios: LiveData<List<Comentario>> get() = _comentarios

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loadComentarios(recetaNombre: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = ComentarioRepository.getComentarios(recetaNombre)
            if (result is Resource.Success<*>) {
                _comentarios.value = result.data as List<Comentario>
            } else {
            }
            _loading.value = false
        }
    }

    fun sendMessage(recetaNombre: String, contenido: String, estrellas: Float) {
        viewModelScope.launch {
            val currentMillis = System.currentTimeMillis()
            val currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentMillis), ZoneId.systemDefault())
            val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val autor = obtenerNombreAutor()
            if (autor != null) {
                val comentario = Comentario(autor, "", contenido, formattedDateTime, estrellas) // Incluir estrellas en el objeto Comentario

                val result = ComentarioRepository.agregarComentario(recetaNombre, comentario)
                if (result is Resource.Success<*>) {
                    loadComentarios(recetaNombre)
                }
            }
        }
    }

    suspend fun obtenerNombreAutor(): String {
        val currentUser = auth.currentUser
        val email = currentUser?.email ?: return "Unknown"

        val perfilRepository = PerfilRepository()
        return try {
            val perfil = perfilRepository.getPerfilSuspend(email)
            perfil?.nombre ?: email.substringBefore("@")
        } catch (e: Exception) {
            email.substringBefore("@")
        }
    }

}