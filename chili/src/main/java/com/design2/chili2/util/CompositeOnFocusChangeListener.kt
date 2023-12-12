package com.design2.chili2.util

import android.view.View

class CompositeOnFocusChangeListener : View.OnFocusChangeListener {

    private val listeners = mutableListOf<View.OnFocusChangeListener>()

    fun addListener(listener: View.OnFocusChangeListener) {
        listeners.add(listener)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        listeners.forEach { it.onFocusChange(v, hasFocus) }
    }
}