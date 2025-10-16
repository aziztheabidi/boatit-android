package com.boatit.boatsharing.application

import android.app.Application
import android.content.Context
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.di.Modules
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
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
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
            }
        }

        startKoin {
            modules(Modules)
            androidContext(this@MainApplication)
        }
    }
}

