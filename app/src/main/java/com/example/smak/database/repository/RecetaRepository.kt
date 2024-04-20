package com.example.smak.database.repository

import com.example.smak.data.Receta
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecetaRepository  {
    companion object{

        private val db = FirebaseFirestore.getInstance()

        fun agregarReceta(receta: Receta): Resource {
            return try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val autor = currentUser?.email

                val imagenesStrings = receta.imagenes.map { it.toString() }

                val nuevaReceta = hashMapOf(
                    "id" to receta.id,
                    "nombre" to receta.nombre,
                    "ingredientes" to receta.ingredientes,
                    "pasos" to receta.pasos,
                    "duracion" to receta.duracion,
                    "tipo" to receta.tipo,
                    "autor" to autor,
                    "imagenes" to  imagenesStrings
                )

                db.collection("recetas").document(receta.nombre).set(nuevaReceta)
                val nuevaRecetaConAutor = autor?.let { receta.copy(autor = it) }
                Resource.Success(nuevaRecetaConAutor)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

         suspend fun getAllRecetas(): Resource {
            return try {
                val querySnapshot = db.collection("recetas").get().await()
                val recetasList = mutableListOf<Receta>()
                for (document in querySnapshot.documents) {
                    val nombre = document.getString("nombre") ?: ""
                    val ingredientes = document.getString("ingredientes") ?: ""
                    val pasos = document.getString("pasos") ?: ""
                    val tiempo = document.getString("tiempo") ?: ""
                    val tipo = document.getString("tipo") ?: ""
                    val imagenesList = document.get("imagenes") as? List<String> ?: emptyList()
                    val autor = document.getString("autor") ?: ""
                    val receta = Receta(nombre, ingredientes, pasos, tiempo, tipo, autor, imagenesList.toMutableList())
                    recetasList.add(receta)
                }
                Resource.Success(recetasList)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }
}