package com.example.smak.database.repository

import android.util.Log
import com.example.smak.compras.data.Compras
import com.example.smak.data.Receta
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecetaRepository  {
   /* companion object{

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
    }*/

    companion object {
        private val db = FirebaseFirestore.getInstance()

        // Agregar receta para todos los usuarios y para el usuario actual
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

                    // Devolver el resultado exitoso
                    Resource.Success("Receta agregada para todos los usuarios")
                } else {
                    // Si el usuario no está autenticado, devolver un error
                    Resource.Error(Exception("Usuario no autenticado"))
                }
            } catch (e: Exception) {
                // Manejar cualquier excepción y devolver un error
                Resource.Error(e)
            }
        }

        // Método privado para agregar la receta a la colección de recetas del usuario actual
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

        // Obtener todas las recetas del usuario actual


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
        /*suspend fun getMisRecetas(): Resource {
            return try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val autor = currentUser?.email
                if (autor != null) {
                    val querySnapshot = db.collection("users").document(autor).collection("privadas").get().await()
                    val recetasList = mutableListOf<Receta>()
                    for (document in querySnapshot.documents) {
                        val receta = document.toObject(Receta::class.java)
                        receta?.let { recetasList.add(it) }
                    }
                    Resource.Success(recetasList)
                } else {
                    Resource.Error(Exception("Usuario no encontrado"))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }*/

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



}