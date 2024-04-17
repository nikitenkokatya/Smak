package com.example.smak.database.repository

import com.example.smak.data.Receta
import com.example.smak.database.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RecetaRepository constructor() {
    companion object{

        private val db = FirebaseFirestore.getInstance()

        /** fun agregarReceta(receta: Receta): Resource {
            return try {
                val documentReference = db.collection("recetas").add(receta).await()
                val newReceta = Receta(
                    nombre = receta.nombre,
                    ingredientes = receta.ingredientes,
                    pasos = receta.pasos,
                    duracion = receta.duracion,
                    tipo = receta.tipo,
                    autor = receta.autor
                )
                Resource.Success(newReceta)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }*/

        fun agregarReceta(receta: Receta): Resource {
            return try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val autor = currentUser?.email

                val nuevaReceta = hashMapOf(
                    "id" to receta.id,
                    "nombre" to receta.nombre,
                    "ingredientes" to receta.ingredientes,
                    "pasos" to receta.pasos,
                    "duracion" to receta.duracion,
                    "tipo" to receta.tipo,
                    "autor" to autor
                )

                db.collection("recetas").document(receta.nombre).set(nuevaReceta)
                val nuevaRecetaConAutor = autor?.let { receta.copy(autor = it) }
                Resource.Success(nuevaRecetaConAutor)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }


        /*fun addFavouriteCrypto(crypto: CryptoCurrency) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.email?.let { userEmail ->
                val userFavoritesRef = db.collection("users").document(userEmail)
                    .collection("favourites").document(crypto.name)

                userFavoritesRef.set(crypto)
                    .addOnSuccessListener {
                        favouritesCrypto.add(crypto)
                        Log.d("TAG", "Favorite added to Firestore for user: $userEmail")
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding favorite to Firestore for user: $userEmail", e)
                    }
            }
        }*/
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
                    val autor = document.getString("autor") ?: ""
                    val receta = Receta(nombre, ingredientes, pasos, tiempo, tipo,  autor)
                    recetasList.add(receta)
                }
                Resource.Success(recetasList)
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }
}