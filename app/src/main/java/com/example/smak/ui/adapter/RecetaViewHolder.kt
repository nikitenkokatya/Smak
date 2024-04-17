package com.example.smak.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemLayoutBinding

class RecetaViewHolder(val binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(item: Receta){
        binding.txtnombrereceta.text = item.nombre
        //Glide.with(binding.root)
          //  .load(item.imagenes) // Aquí asumo que tienes la URI de la imagen en tu objeto Receta
            //.into(binding.rvFotos)
    }
}