package com.example.smak.data

import android.os.Parcel
import android.os.Parcelable

data class Receta(
    var nombre: String = "",
    var ingredientes: String = "",
    var pasos: String = "",
    var duracion: String = "",
    var tipo: String = "",
    var autor: String = "",
    var imagenes: MutableList<String> = mutableListOf(),
    val comentarios: MutableList<Comentario> = mutableListOf()
):Parcelable , RecetaItem {
    override val id: Int = 0 // O el ID adecuado si tienes uno
    override val title: String
    get() = nombre
    override val image: String
    get() = imagenes.firstOrNull() ?: ""
    companion object {
        const val TAG = "Receta"

        @JvmField
        val CREATOR = object : Parcelable.Creator<Receta> {
            override fun createFromParcel(parcel: Parcel): Receta {
                return Receta(parcel)
            }

            override fun newArray(size: Int): Array<Receta?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!.toMutableList(),
        mutableListOf()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(nombre)
        dest.writeString(ingredientes)
        dest.writeString(pasos)
        dest.writeString(duracion)
        dest.writeString(tipo)
        dest.writeString(autor)
        dest.writeStringList(imagenes)
    }
}
