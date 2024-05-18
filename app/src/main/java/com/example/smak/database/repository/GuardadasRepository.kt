package com.example.smak.database.repository

import android.util.Log
import com.example.smak.data.Receta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GuardadasRepository {
    private val db = FirebaseFirestore.getInstance()
    var recetasFavoritas = mutableSetOf<Receta>()

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    fun addRecetaFavorita(receta: Receta) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.email?.let { userEmail ->
            val userFavoritesRef = db.collection("users").document(userEmail)
                .collection("favoritos").document(receta.nombre)

            userFavoritesRef.set(receta)
                .addOnSuccessListener {
                    recetasFavoritas.add(receta)
                    Log.d("TAG", "Favorite added to Firestore for user: $userEmail")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding favorite to Firestore for user: $userEmail", e)
                }
        }
    }
    fun getRecetaFavorita(onSuccess: () -> Unit) {
        recetasFavoritas = mutableSetOf()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.email?.let { userEmail ->
            db.collection("users").document(userEmail)
                .collection("favoritos")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val nombre = document.getString("nombre") ?: ""
                        val ingredientes = document.getString("ingredientes") ?: ""
                        val pasos = document.getString("pasos") ?: ""
                        val tiempo = document.getString("duracion") ?: ""
                        val tipo = document.getString("tipo") ?: ""
                        val imagenesList = document.get("imagenes") as? List<String> ?: emptyList()
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
                        recetasFavoritas.add(receta)
                    }
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                }
        }
    }

    fun borrarRecetaFavorita(receta: Receta){
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("users").document(email)
                    .collection("favoritos")
                    .document(receta.nombre)
                    .delete()
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }
    }
}