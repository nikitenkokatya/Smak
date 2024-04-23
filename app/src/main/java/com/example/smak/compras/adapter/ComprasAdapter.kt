package com.example.smak.compras.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.compras.data.Compras
import com.example.smak.databinding.ItemComprasBinding

class ComprasAdapter(private val listener:onClickListener):
    ListAdapter<Compras, ComprasViewHolder>(COMPRAS_COMPARATOR) {
    interface onClickListener{
        fun onClickDelete(compras: Compras):Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ComprasViewHolder(ItemComprasBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ComprasViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)

        holder.binding.root.setOnLongClickListener {
            listener.onClickDelete(item)
            true
        }
    }
    companion object{
        val COMPRAS_COMPARATOR = object : DiffUtil.ItemCallback<Compras>(){
            override fun areItemsTheSame(oldItem: Compras, newItem: Compras): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Compras, newItem: Compras): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}