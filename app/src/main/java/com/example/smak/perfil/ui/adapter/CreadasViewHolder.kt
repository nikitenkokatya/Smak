package com.example.smak.perfil.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Receta
import com.example.smak.databinding.PerfilLayoutBinding


class CreadasViewHolder (val binding: PerfilLayoutBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: Receta, context: Context){
        val img = ImageView(context)
        img.setImageBitmap(base64ToBitmap(item.imagenes[0]))

        binding.llParent.addView(img);


        val screenWidth: Int = context.getResources().getDisplayMetrics().widthPixels
        val imageViewWidth = screenWidth / 3

        val layoutParams = ConstraintLayout.LayoutParams(
            imageViewWidth,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        img.setLayoutParams(layoutParams)

        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        img.setLayoutParams(layoutParams)
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}