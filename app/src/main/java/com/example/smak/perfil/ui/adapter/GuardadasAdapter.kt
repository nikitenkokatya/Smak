package com.example.smak.perfil.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.Locator
import com.example.smak.data.Receta
import com.example.smak.databinding.PerfilLayoutBinding

class GuardadasAdapter(private val listener: onClickGuardadas): ListAdapter<Receta, GuardadasViewHolder>(
    GUARDAR_COMPARATOR) {

    interface onClickGuardadas {
        fun onClickDetails(receta: Receta)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardadasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  GuardadasViewHolder(PerfilLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: GuardadasViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, Locator.requieredApplication)

        holder.binding.root.setOnClickListener() {
            listener.onClickDetails(item)
        }
    }

    companion object {
        val GUARDAR_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}