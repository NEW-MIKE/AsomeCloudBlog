package com.example.astroclient

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class App : Application()  {

    companion object {
        lateinit var instance: Application
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}