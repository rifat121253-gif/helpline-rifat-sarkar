package com.example

import android.app.Application
import com.example.data.FirestoreManager

class HelplineApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firestore client in the main application module
        FirestoreManager.initialize(this)
    }
}
