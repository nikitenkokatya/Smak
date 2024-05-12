package com.example.smak.compras.adapter




import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.smak.R
import com.example.smak.data.Compras
import com.example.smak.databinding.ItemComprasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ComprasAdapter(private val listener:onClickListener):
    ListAdapter<Compras, ComprasViewHolder>(COMPRAS_COMPARATOR), ItemTouchHelperAdapter {
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


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
    }


    override fun onItemDismiss(position: Int) {
        val item = currentList[position]
        listener.onClickDelete(item)
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                notifyItemChanged(position)
            }
        }
    }
}

