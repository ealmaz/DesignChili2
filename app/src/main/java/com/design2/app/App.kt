package com.design2.app

import android.app.Application
import com.design2.chili2.util.secured.ApplicationSecureGestureDelegate
import com.example.myapplication.secure_view_component.contracts.OnApplicationSecureGestureListener

class App: Application(), OnApplicationSecureGestureListener by ApplicationSecureGestureDelegate() {

    override fun onCreate() {
        super.onCreate()
        onApplicationCreated(this, getSharedPreferences("secure_prefs", MODE_PRIVATE))
    }
}