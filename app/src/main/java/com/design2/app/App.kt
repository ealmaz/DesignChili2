package com.design2.app

import android.app.Application
import com.design2.chili2.util.secured.ApplicationSecureGestureDelegate
import com.design2.chili2.util.secured.OnApplicationSecureGestureListener

class App: Application(), OnApplicationSecureGestureListener by ApplicationSecureGestureDelegate() {

    override fun onCreate() {
        super.onCreate()
        onApplicationCreated(this)
        updateSecureGestureState(true)
    }
}