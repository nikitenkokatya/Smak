package com.example.smak.database.repository

import android.util.Log
import com.example.smak.compras.data.Compras
import com.example.smak.data.Receta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await as await

class CreadasRepository {
    /*private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun crearReceta(receta: Receta) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                val imagenesStrings = receta.imagenes.map { it.toString() }

                val nuevaReceta = hashMapOf(
                    "id" to receta.id,
                    "nombre" to receta.nombre,
                    "ingredientes" to receta.ingredientes,
                    "pasos" to receta.pasos,
                    "duracion" to receta.duracion,
                    "tipo" to receta.tipo,
                    "autor" to email,
                    "imagenes" to  imagenesStrings
                )
                db.collection("creadas").document(email)
                    .collection("receta")
                    .document(receta.nombre).set(receta)
            }
        }
    }

    fun getList(callback: (List<Receta>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("creadas").document(email)
                    .collection("receta")
                    .get()
                    .addOnSuccessListener { documents ->
                        val querySnapshot = db.collection("creadas").get().await()
                        val recetasList = mutableListOf<Receta>()
                        for (document in querySnapshot.documents) {
                            val nombre = document.getString("nombre") ?: ""
                            val ingredientes = document.getString("ingredientes") ?: ""
                            val pasos = document.getString("pasos") ?: ""
                            val tiempo = document.getString("tiempo") ?: ""
                            val tipo = document.getString("tipo") ?: ""
                            val imagenesList =
                                document.get("imagenes") as? List<String> ?: emptyList()
                            val autor = document.getString("autor") ?: ""
                            val receta = Receta(
                                nombre,
                                ingredientes,
                                pasos,
                                tiempo,
                                tipo,
                                autor,
                                imagenesList.toMutableList()
                            )
                            recetasList.add(receta)
                        }
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }
    }*/
}