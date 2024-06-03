package com.example.smak.database.repository

import android.util.Log
import com.example.smak.data.Comentario
import com.example.smak.data.Receta
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ComentarioRepository {
    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val auth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }
        suspend fun getComentarios(recetaNombre: String): Resource {
            return try {
                val documentRef = db.collection("recetas").document(recetaNombre)
                val querySnapshot = documentRef.collection("comentarios").get().await()
                val comentarios = querySnapshot.documents.mapNotNull { it.toObject(Comentario::class.java) }
                Resource.Success(comentarios)
            } catch (e: Exception) {
                Resource.Error(Exception("Error al obtener comentarios: ${e.message}"))
            }
        }

        suspend fun agregarComentario(recetaNombre: String, comentario: Comentario): Resource {
            return try {
                val documentRef = db.collection("recetas").document(recetaNombre)
                val comentarioRef = documentRef.collection("comentarios").document(auth.currentUser!!.email.toString())

                val comentarioData = hashMapOf(
                    "autor" to comentario.autor,
                    "email" to auth.currentUser!!.email.toString(),
                    "contenido" to comentario.contenido,
                    "fecha" to comentario.fecha,
                    "estrellas" to comentario.estrellas
                )

                comentarioRef.set(comentarioData).await()
                Resource.Success("Comentario agregado")
            } catch (e: Exception) {
                Resource.Error(Exception("Error al agregar comentario: ${e.message}"))
            }
        }
    }
}
