package com.example.smak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.data.Receta
import com.example.smak.databinding.PerfilLayoutBinding
import com.example.smak.ui.adapter.RecetaViewHolder

class CreadasAdapter(): ListAdapter<Receta, CreadasViewHolder>(CREATE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreadasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CreadasViewHolder(PerfilLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: CreadasViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, Locator.requieredApplication)
    }

    companion object {
        val CREATE_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}