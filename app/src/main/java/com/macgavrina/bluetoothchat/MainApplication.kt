package com.macgavrina.bluetoothchat

import android.app.Application
import android.content.Context

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: MainApplication
            private set

        fun applicationContext() : Context {
            return instance.applicationContext
        }

    }

}