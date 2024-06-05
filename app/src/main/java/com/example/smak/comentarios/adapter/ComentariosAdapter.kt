package com.example.smak.comentarios.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.data.Comentario
import com.example.smak.databinding.ItemMessageBinding

class ComentariosAdapter (private val listener: onClick): ListAdapter<Comentario, ComentarioViewHolder>(
    COMENTARIO_COMPARATOR
) {
    interface onClick {
        fun onClickPersona(comentario: Comentario)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  ComentarioViewHolder(ItemMessageBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)

        holder.binding.root.setOnClickListener() { _ ->
            listener.onClickPersona(item)
        }
    }

    companion object {
        val COMENTARIO_COMPARATOR = object : DiffUtil.ItemCallback<Comentario>() {
            override fun areItemsTheSame(oldItem: Comentario, newItem: Comentario): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Comentario, newItem: Comentario): Boolean {
                return newItem.autor == oldItem.autor
            }
        }
    }
}