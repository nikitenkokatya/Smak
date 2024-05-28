package com.example.smak.data

import android.os.Parcel
import android.os.Parcelable

data class Comentario(
    val autor: String = "",
    val contenido: String = "",
    val fecha: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(autor)
        parcel.writeString(contenido)
        parcel.writeString(fecha)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comentario> {
        override fun createFromParcel(parcel: Parcel): Comentario {
            return Comentario(parcel)
        }

        override fun newArray(size: Int): Array<Comentario?> {
            return arrayOfNulls(size)
        }
    }
}