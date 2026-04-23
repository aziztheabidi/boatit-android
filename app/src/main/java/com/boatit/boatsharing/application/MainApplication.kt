package com.boatit.boatsharing.application

import android.app.Application
import android.content.Context
import com.boatit.boatsharing.R
import com.boatit.boatsharing.data.network.di.Modules
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        val apiKey = getString(R.string.mapAPiKey)
        if (apiKey.isNotBlank() && !Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
            }
        }

        startKoin {
            androidContext(this@MainApplication)
            modules(Modules)
        }
    }
}
