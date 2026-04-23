package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.fcm.FirebaseNotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Firebase SDK singletons and Firestore-backed push helper. */
val firebaseSdkModule =
    module {
        single { FirebaseAuth.getInstance() }
        single { FirebaseDatabase.getInstance() }
        single { FirebaseMessaging.getInstance() }
        single { FirebaseFirestore.getInstance() }

        single { FirebaseNotificationService(get(), androidContext()) }
    }
