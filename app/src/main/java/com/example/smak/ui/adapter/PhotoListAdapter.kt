package com.example.smak.ui.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.smak.data.Receta
import com.example.smak.databinding.ItemFotoBinding
import com.example.smak.databinding.ItemFotolistBinding

class PhotoListAdapter(private val photos: List<String>) :
    RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var listener: OnItemChangedListener? = null

    interface OnItemChangedListener {
        fun onItemChanged(position: Int, listSize: Int)
    }

    fun setOnItemChangedListener(listener: OnItemChangedListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoListViewHolder(ItemFotolistBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.layoutManager = recyclerView.layoutManager as LinearLayoutManager

        this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE,
                    RecyclerView.SCROLL_STATE_SETTLING,
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        listener?.onItemChanged(
                            layoutManager.findFirstVisibleItemPosition() + 1,
                            photos.size
                        )

                    }
                }
            }
        })
    }

    override fun getItemCount(): Int = photos.size

    class PhotoListViewHolder(private val binding: ItemFotolistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(base64: String) {
            binding.imageView2.setImageBitmap(base64ToBitmap(base64))
        }

        fun base64ToBitmap(base64String: String): Bitmap {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}