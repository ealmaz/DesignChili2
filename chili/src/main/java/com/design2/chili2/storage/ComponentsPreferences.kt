package com.design2.chili2.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ComponentsPreferences(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)


    fun saveIsExpandableContainerExpanded(containerId: String, isExpanded: Boolean) {
        pref.edit { putBoolean(containerId, isExpanded) }
    }

    fun getIsExpandableContainerExpanded(containerId: String): Boolean {
        return pref.getBoolean(containerId, true)
    }

    fun resetAll() {
        pref.edit(commit = true) { clear() }
    }

    companion object {

        const val STORAGE_NAME = "com.design2.chili2.storage.ComponentsPreferences"

        @Volatile
        private var instance: ComponentsPreferences? = null

        fun getInstance(context: Context): ComponentsPreferences {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ComponentsPreferences(context)
                    }
                }
            }
            return instance!!
        }
    }
}