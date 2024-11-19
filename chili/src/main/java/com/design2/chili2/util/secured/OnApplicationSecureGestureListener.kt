package com.example.myapplication.secure_view_component.contracts

import android.content.Context
import android.content.SharedPreferences

interface OnApplicationSecureGestureListener {
    val BROADCAST_TAG: String get() = "UPDATE_SECURE_STATE"
    fun onApplicationCreated(context: Context, sharedPreferences: SharedPreferences)
    fun isSecuredNow(): Boolean
    fun switchSecuredState()
}