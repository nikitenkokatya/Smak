package com.example.smak.comentarios.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.Locator
import com.example.smak.data.Receta
import com.example.smak.databinding.PerfilLayoutBinding

class PerosonaAdapter(private val listener: onClickCreadas) :
    ListAdapter<Receta, PRecetaViewHolder>(LIST_COMPARATOR) {

    interface onClickCreadas {
        fun onClickDetailsC(receta: Receta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRecetaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PRecetaViewHolder(PerfilLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PRecetaViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, Locator.requieredApplication)

        holder.binding.root.setOnClickListener() {
            listener.onClickDetailsC(item)
        }
    }

    companion object {
        val LIST_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}

