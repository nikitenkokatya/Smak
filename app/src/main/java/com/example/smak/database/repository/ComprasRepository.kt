package com.example.smak.database.repository

import android.util.Log
import com.example.smak.data.Compras
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ComprasRepository {
        private val db: FirebaseFirestore by lazy {
            Firebase.firestore
        }
        private val auth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }

        fun agregarIngrediente(nombre: String) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userEmail = currentUser.email
                userEmail?.let { email ->
                    val ingrediente = hashMapOf(
                        "nombre" to nombre
                    )
                    db.collection("users").document(email)
                        .collection("ingredientes")
                        .document(nombre).set(ingrediente)
                }
            }
        }

    fun getList(callback: (List<Compras>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("users").document(email)
                    .collection("ingredientes")
                    .get()
                    .addOnSuccessListener { documents ->
                        val listaIngredientes = mutableListOf<Compras>()
                        for (document in documents) {
                            val nombre = document.getString("nombre") ?: ""
                            val ingrediente = Compras(nombre)
                            listaIngredientes.add(ingrediente)
                        }
                        callback(listaIngredientes)
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }
    }

    fun borrarIngrediente(compras: Compras) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("users").document(email)
                    .collection("ingredientes")
                    .document(compras.nombre)
                    .delete()
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ComprasRepository", "Error al borrar el ingrediente: ${exception.message}")
                    }
            }
        }
    }
}