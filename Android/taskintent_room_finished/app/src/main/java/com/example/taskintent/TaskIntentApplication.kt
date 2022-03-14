package com.example.taskintent

import android.app.Application

class TaskIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)

    }
}