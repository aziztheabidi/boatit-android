package com.boatit.boatsharing.application

import android.app.Application
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.di.Modules
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val apiKey = getString(R.string.mapAPiKey)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("token:" + token)
            }
        }
        startKoin {
            modules(Modules)
            androidContext(this@MainApplication)
        }

    }
}
