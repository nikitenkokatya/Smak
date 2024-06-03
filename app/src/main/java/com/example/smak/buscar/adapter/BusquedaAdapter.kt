package com.example.smak.buscar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.data.RecetaAPI
import com.example.smak.data.RecetaItem
import com.example.smak.databinding.ItemBusquedaBinding

class BusquedaAdapter ( private val recipeList: List<RecetaItem>, private val listener: onClick): ListAdapter<RecetaItem, BusquedaViewHolder>(RECETA_COMPARATOR) {
    interface onClick {
        fun onClickDetails(receta: RecetaItem)
        fun userOnLongClick(receta: RecetaItem): Boolean
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusquedaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  BusquedaViewHolder(ItemBusquedaBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: BusquedaViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)

        holder.binding.clItem.setOnClickListener() { _ ->
            listener.onClickDetails(item)
        }
    }

    companion object {
        val RECETA_COMPARATOR = object : DiffUtil.ItemCallback<RecetaItem>() {
            override fun areItemsTheSame(oldItem: RecetaItem, newItem: RecetaItem): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: RecetaItem, newItem: RecetaItem): Boolean {
                return newItem.id == oldItem.id
            }
        }
    }
}