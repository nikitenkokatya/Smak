package com.example.smak.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemLayoutBinding

class RecetaViewHolder(val binding: ItemLayoutBinding, private val listener: RecetaAdapter.onClick) : RecyclerView.ViewHolder(binding.root),
    PhotoListAdapter.OnItemChangedListener {
    fun bind(item: Receta, rvFotos: RecyclerView) {
        binding.txtnombrereceta.text = item.nombre
        binding.txtautor.text = "by " +item.autor

        val adapter = PhotoListAdapter(item.imagenes)

        binding.rvFotos.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = adapter
        }

        binding.txtcountUI.text = "1/${item.imagenes.size}"

        adapter.setRecyclerView(rvFotos)

        adapter.setOnItemChangedListener(this)

        binding.btnComment.setOnClickListener {
            listener.onCommentButtonClick(item)
        }
    }

    override fun onItemChanged(position: Int, listSize: Int) {
        binding.txtcountUI.text = "$position/$listSize"
    }
}