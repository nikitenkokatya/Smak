package com.example.smak.utils
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smak.MainActivity
import com.example.smak.R

fun createNotificationChannel(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channel = NotificationChannel(
        MainActivity.CHANNEL_ID,
        "Important Notification Channel",
        NotificationManager.IMPORTANCE_HIGH,
    ).apply {
        description = "This notification contains important announcement, etc."
    }
    notificationManager.createNotificationChannel(channel)
}

@SuppressLint("MissingPermission")
fun sendNotification(context:Context,pendingIntent: PendingIntent, title: String, textContext: String) {

    val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(textContext)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        //.setContentIntent(PendingIntent.getActivity(requireContext(), 0,  Intent(),PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)){
        notify(getUniqueId(), builder.build())
    }
}
private fun getUniqueId() = (System.currentTimeMillis() % 1000).toInt()
