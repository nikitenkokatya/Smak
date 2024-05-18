package com.example.smak.database.repository

import com.example.smak.data.Perfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PerfilRepository {
    private val db = FirebaseFirestore.getInstance()
    companion object {
        var userPerfil : Perfil? = null
    }

    fun getPerfil(userEmail: String, onSuccess:() -> Unit, onFailure:() -> Unit) {
        db.collection("users")
            .document(userEmail)
            .collection("perfil")
            .document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombre = document.getString("userName")
                    val foto = document.getString("foto")

                    userPerfil = Perfil(nombre, foto)
                    onSuccess()
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                onFailure()
            }
    }

    fun guardarPerfil(perfil: Perfil) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val autor = currentUser?.email

        val perfilHashmap = hashMapOf(
            "userName" to perfil.nombre,
            "foto" to perfil.foto
        )

        db.collection("users").document(autor!!).collection("perfil")
            .document(autor)
            .set(perfilHashmap)
    }
}