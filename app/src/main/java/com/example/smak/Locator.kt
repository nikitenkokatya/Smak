package com.example.smak

import android.app.Application
import android.content.Context

object Locator {
    private var application: Application? = null
    val requieredApplication get() = application?: error("Missing call initWith")
    fun initWith(application: Application){
        Locator.application = application
    }
}