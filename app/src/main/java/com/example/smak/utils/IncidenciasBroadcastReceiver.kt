package com.example.smak.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.smak.MainActivity

class IncidenciasBroadcastReceiver:BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder = Notification.Builder(context, MainActivity.CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .setContentText("Se ha creado")
            .setContentTitle("Incidencias")

        NotificationManagerCompat.from(context!!).notify(1, builder.build())
    }
}