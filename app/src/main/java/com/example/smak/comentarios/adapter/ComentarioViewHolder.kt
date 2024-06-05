package com.example.smak.comentarios.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Comentario
import com.example.smak.databinding.ItemMessageBinding

class ComentarioViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comentario: Comentario) {
        binding.senderTextView.text = comentario.autor
        binding.messageTextView.text = comentario.contenido
        binding.txtfecha.text = comentario.fecha
        binding.estrellas.rating = comentario.estrellas
    }
}
