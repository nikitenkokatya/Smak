package com.example.smak.database.repository

import android.util.Log
import com.example.smak.data.Receta
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecetaRepository  {
    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun agregarReceta(receta: Receta): Resource {
            return try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val autor = currentUser?.email

                agregarRecetaParaTodos(receta)

                if (autor != null) {
                    Log.d("RecetaRepository", "Agregar receta al perfil del usuario")
                    agregarRecetaAlPerfilUsuario(receta, autor)
                }

                val nuevaRecetaConAutor = autor?.let { receta.copy(autor = it) }
                Resource.Success(nuevaRecetaConAutor)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

        fun agregarRecetaParaTodos(receta: Receta): Resource {
            return try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val autor = currentUser?.email
                Log.d("RecetaRepository", "Agregar receta para todos los usuarios")

                if (autor != null) {
                    val imagenesStrings = receta.imagenes.map { it.toString() }

                    val nuevaReceta = hashMapOf(
                        "id" to receta.id,
                        "nombre" to receta.nombre,
                        "ingredientes" to receta.ingredientes,
                        "pasos" to receta.pasos,
                        "duracion" to receta.duracion,
                        "tipo" to receta.tipo,
                        "autor" to autor, // Indicar que la receta es para todos los usuarios
                        "imagenes" to imagenesStrings
                    )

                    db.collection("recetas").document(receta.nombre).set(nuevaReceta)

                    Resource.Success("Receta agregada para todos los usuarios")
                } else {
                    Resource.Error(Exception("Usuario no autenticado"))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

        private fun agregarRecetaAlPerfilUsuario(receta: Receta, autor: String) {
            val imagenesStrings = receta.imagenes.map { it.toString() }

            val nuevaReceta = hashMapOf(
                "id" to receta.id,
                "nombre" to receta.nombre,
                "ingredientes" to receta.ingredientes,
                "pasos" to receta.pasos,
                "duracion" to receta.duracion,
                "tipo" to receta.tipo,
                "autor" to autor,
                "imagenes" to imagenesStrings
            )
            db.collection("users").document(autor).collection("privadas")
                .document(receta.nombre)
                .set(nuevaReceta)

        }

        suspend fun getAllRecetas(): Resource {
            return try {
                val querySnapshot = db.collection("recetas").get().await()
                val recetasList = mutableListOf<Receta>()
                for (document in querySnapshot.documents) {
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
                    recetasList.add(receta)
                }
                Resource.Success(recetasList)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    fun getMisRecetas(callback: (List<Receta>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("users").document(userEmail).collection("privadas").get()
                    .addOnSuccessListener { documents ->
                        val recetasprivadas = mutableListOf<Receta>()
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
                            recetasprivadas.add(receta)
                        }
                        callback(recetasprivadas)
                    }
                    .addOnFailureListener { exception ->
                    }
            }
        }
    }
    fun borrarMiReceta(receta: Receta) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            userEmail?.let { email ->
                db.collection("recetas").document(receta.nombre)
                    .delete()
                    .addOnSuccessListener {
                        db.collection("users").document(email)
                            .collection("privadas")
                            .document(receta.nombre)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("RecetaRepository", "Receta borrada exitosamente")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("RecetaRepository", "Error al borrar la receta del perfil de usuario: ${exception.message}")
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("RecetaRepository", "Error al borrar la receta de la lista general: ${exception.message}")
                    }
            }
        }
    }
}