package com.example.smak.data

import android.net.Uri

data class Receta(
    var nombre:String, var ingredientes:String,
    var pasos:String, var duracion:String, var tipo:String, var autor:String, var imagenes: MutableList<String> = mutableListOf()
) {
    var id:Int = 0
}