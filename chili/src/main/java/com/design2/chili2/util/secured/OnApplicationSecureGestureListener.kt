package com.design2.chili2.util.secured

import android.content.Context
import android.content.SharedPreferences

interface OnApplicationSecureGestureListener {
    val BROADCAST_TAG: String get() = "UPDATE_SECURE_STATE"
    fun onApplicationCreated(context: Context)
    fun isSecuredNow(): Boolean
    fun switchSecuredState()
    fun updateSecureGestureState(isWorking: Boolean)
}