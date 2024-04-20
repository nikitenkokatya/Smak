package com.example.smak.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemLayoutBinding

class RecetaViewHolder(val binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(item: Receta){
        binding.txtnombrereceta.text = item.nombre
        binding.txtautor.text = "by "+item.autor
        val adapter = PhotoListAdapter(item.imagenes)
        binding.rvFotos.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = adapter
        }
    }
}