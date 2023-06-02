package com.example.email.database

import android.app.Application

class EmailIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EmailRepository.initialize(this)
    }
}