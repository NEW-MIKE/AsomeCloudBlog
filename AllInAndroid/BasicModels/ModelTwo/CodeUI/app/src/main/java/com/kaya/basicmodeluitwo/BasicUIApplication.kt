package com.kaya.basicmodeluitwo

import android.app.Application
import android.content.Context

class BasicUIApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }
    companion object {
        lateinit var context: Context
    }
}