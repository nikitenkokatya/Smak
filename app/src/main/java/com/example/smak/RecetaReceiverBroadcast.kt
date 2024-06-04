package com.example.smak

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class RecetaReceiverBroadcast : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val nombreReceta = intent?.getStringExtra("nombre_receta")
        val autor = intent?.getStringExtra("autor")
        val builder = NotificationCompat.Builder(context!!, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Nueva receta!!!")
            .setContentText("${nombreReceta} by ${autor}")
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context!!)){
            notify(1, builder.build())
        }
    }
}