package com.design2.chili2.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ChiliComponentsPreferences(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)

    var isTextViewsSecuredNow: Boolean
        get() = pref.getBoolean(Keys.IS_TEXT_VIEWS_SECURED_NOW, false)
        set(value) = pref.edit { putBoolean(Keys.IS_TEXT_VIEWS_SECURED_NOW, value) }

    var isSecureGestureWorking: Boolean
        get() = pref.getBoolean(Keys.IS_SECURE_GESTURE_WORKING, false)
        set(value) = pref.edit { putBoolean(Keys.IS_SECURE_GESTURE_WORKING, value) }

    fun saveIsExpandableContainerExpanded(containerId: String, isExpanded: Boolean) {
        pref.edit { putBoolean(containerId, isExpanded) }
    }

    fun getIsExpandableContainerExpanded(containerId: String): Boolean {
        return pref.getBoolean(containerId, true)
    }

    fun resetAll() {
        pref.edit(commit = true) { clear() }
    }

    private object Keys {
        const val IS_TEXT_VIEWS_SECURED_NOW = "isTextViewsSecuredNow"
        const val IS_SECURE_GESTURE_WORKING = "isSecureGestureWorking"
    }

    companion object {

        const val STORAGE_NAME = "com.design2.chili2.storage.ComponentsPreferences"

        @Volatile
        private var instance: ChiliComponentsPreferences? = null

        fun getInstance(context: Context): ChiliComponentsPreferences {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ChiliComponentsPreferences(context)
                    }
                }
            }
            return instance!!
        }
    }
}