package com.example.smak.buscar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.data.Receta
import com.example.smak.data.RecetaAPI
import com.example.smak.databinding.ItemBusquedaBinding
import com.example.smak.databinding.ItemLayoutBinding
import com.example.smak.ui.adapter.RecetaViewHolder

class BusquedaAdapter ( private val recipeList: List<RecetaAPI>, private val listener: onClick): ListAdapter<RecetaAPI, BusquedaViewHolder>(RECETA_COMPARATOR) {
    interface onClick {
        fun onClickDetails(receta: RecetaAPI)
        fun userOnLongClick(receta: RecetaAPI): Boolean
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
        val RECETA_COMPARATOR = object : DiffUtil.ItemCallback<RecetaAPI>() {
            override fun areItemsTheSame(oldItem: RecetaAPI, newItem: RecetaAPI): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: RecetaAPI, newItem: RecetaAPI): Boolean {
                return newItem.id == oldItem.id
            }
        }
    }
}