package com.example.smak.buscar.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.Locator
import com.example.smak.data.RecetaAPI
import com.example.smak.databinding.ItemBusquedaBinding

class BusquedaViewHolder (val binding: ItemBusquedaBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: RecetaAPI){
        binding.txtnombrereceta.text = item.title
        binding.txtautor.text = "by smak"

        Glide.with(Locator.requieredApplication)
            .load(item.image)
            .into(binding.imgFoto)
    }
}