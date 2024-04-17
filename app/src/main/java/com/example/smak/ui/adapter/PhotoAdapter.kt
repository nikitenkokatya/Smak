package com.example.smak.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.smak.databinding.ItemfotoBinding

class PhotoAdapter (private val photos: List<Uri>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoViewHolder(ItemfotoBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int = photos.size

    class PhotoViewHolder(private val binding: ItemfotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoUri: Uri) {
            Glide.with(binding.root)
                .load(photoUri)
                .apply(RequestOptions().override(200, 200))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageView2)
        }
    }
}