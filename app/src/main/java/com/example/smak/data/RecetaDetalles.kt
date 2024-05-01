package com.example.smak.data
import retrofit2.Call
import retrofit2.http.GET

data class RecetaDetalles(
    val title: String,
    val readyInMinutes: Int,
    val extendedIngredients: List<Ingredient>,
    val instructions: List<String>
)

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String
)

interface RecipeApi {
    @GET("garlicky-kale-644387") // Ruta de la receta en la API
    fun getRecipe(): Call<RecetaDetalles>
}
