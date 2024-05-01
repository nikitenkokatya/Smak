package com.example.smak.compras.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.compras.data.Compras
import com.example.smak.databinding.ItemComprasBinding

class ComprasViewHolder (val binding: ItemComprasBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Compras){
        binding.cbxIngredientes.text = item.nombre
    }
}