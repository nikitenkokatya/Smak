package com.example.smak.compras.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}


interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}
