package com.example.smak.buscar.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.Locator
import com.example.smak.R
import com.example.smak.data.RecetaAPI
import com.example.smak.data.RecetaItem
import com.example.smak.databinding.ItemBusquedaBinding

class BusquedaViewHolder (val binding: ItemBusquedaBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: RecetaItem){
        binding.txtnombrereceta.text = item.title
        binding.txtautor.text = "by smak"

        // Comprobar si la imagen es una cadena Base64
        if (item.image.startsWith("data:image")) {
            // Eliminar el encabezado de la cadena Base64
            val base64String = item.image.substring(item.image.indexOf(",") + 1)
            val bitmap = decodeBase64ToBitmap(base64String)
            if (bitmap != null) {
                Glide.with(Locator.requieredApplication)
                    .load(bitmap)
                    .into(binding.imgFoto)
            } else {
               /* // Manejar el caso cuando el bitmap es nulo
                Glide.with(Locator.requieredApplication)
                    .load(R.drawable.placeholder) // Usa una imagen de marcador de posición
                    .into(binding.imgFoto)*/
            }
        } else {
            // Cargar la imagen normalmente si no es Base64
            Glide.with(Locator.requieredApplication)
                .load(item.image)
                .into(binding.imgFoto)
        }
    }


    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            Log.e("BusquedaViewHolder", "Error decoding Base64 string", e)
            null
        }
    }
}