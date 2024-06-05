package com.example.smak.buscar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.data.Tipo
import com.example.smak.databinding.ItemTipoBinding

class RecipeTypeAdapter(
    private val types: List<Tipo>,
    private val onClick: (String) -> Unit) : RecyclerView.Adapter<RecipeTypeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTipoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Tipo) {
            binding.txttipo.text = item.name
            Glide.with(binding.imgftipo.context)
                .load(item.imageRes)
                .into(binding.imgftipo)
            binding.executePendingBindings()
            binding.root.setOnClickListener { onClick(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTipoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(types[position])
    }

    override fun getItemCount() = types.size
}
