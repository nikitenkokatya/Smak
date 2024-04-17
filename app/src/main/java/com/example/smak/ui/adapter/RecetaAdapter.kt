package com.example.smak.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemLayoutBinding

class RecetaAdapter():ListAdapter<Receta, RecetaViewHolder>(RECETA_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  RecetaViewHolder(ItemLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }


    companion object {
        val RECETA_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.id == oldItem.id
            }
        }
    }
}