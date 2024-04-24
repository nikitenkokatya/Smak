package com.example.smak

import DataStorePreferencesRepository
import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.murray.invoice.data.preferences.UserPreferencesRepository


object Locator {
    private var application: Application? = null
    val requieredApplication get() = application?: error("Missing call initWith")
    fun initWith(application: Application){
        Locator.application = application
    }

    private val Context.userStore by preferencesDataStore(name = "user")
    private val Context.settingsStore by preferencesDataStore(name = "settings")

    val userPreferencesRepository by lazy {
        UserPreferencesRepository(requieredApplication.userStore)
    }

    val settingsPreferencesRepository by lazy {
        DataStorePreferencesRepository(requieredApplication.settingsStore)
    }
}