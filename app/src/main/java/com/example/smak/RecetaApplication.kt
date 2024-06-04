package com.example.smak

import android.app.Application
import android.content.IntentFilter
import com.google.firebase.FirebaseApp

class RecetaApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Locator.initWith(this)
        FirebaseApp.initializeApp(this)
        registerReceiver(RecetaReceiverBroadcast(), IntentFilter("com.example.receta"))
    }
}