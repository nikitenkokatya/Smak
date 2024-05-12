package com.example.smak.api

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SpoonacularApiClient {
    private val client = OkHttpClient()

    fun searchRecipes(query: String, number: Int, apiKey: String, callback: (String?) -> Unit) {
        val url = "https://api.spoonacular.com/recipes/complexSearch?apiKey=$apiKey&query=$query&number=$number"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody)
            }
        })
    }

    fun getRecipeDetail(id: Int, apiKey: String, callback: (String?) -> Unit) {
        val url = "https://api.spoonacular.com/recipes/$id/information?apiKey=$apiKey"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody)
            }
        })
    }

    private val BASE_URL = "https://spoonacular.com/recipes/"

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}


