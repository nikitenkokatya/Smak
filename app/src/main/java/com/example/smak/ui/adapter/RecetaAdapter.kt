package com.example.smak.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemLayoutBinding

class RecetaAdapter( private val listener: onClick):ListAdapter<Receta, RecetaViewHolder>(RECETA_COMPARATOR) {

    interface onClick {
        fun onClickDetails(receta: Receta)
        fun userOnLongClick(receta: Receta): Boolean
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  RecetaViewHolder(ItemLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, holder.binding.rvFotos)

        holder.binding.clItem.setOnClickListener() { _ ->
            listener.onClickDetails(item)
        }
    }

    companion object {
        val RECETA_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}