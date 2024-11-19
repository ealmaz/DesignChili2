package com.design2.chili2.util.secured

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View

class MoneySecureTransformMethod private constructor() : TransformationMethod {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return "••••••••"
    }

    override fun onFocusChanged(
        view: View?,
        sourceText: CharSequence?,
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {

    }

    companion object {
        private var instance: MoneySecureTransformMethod? = null

        fun getInstance(): MoneySecureTransformMethod {
            if (instance == null) {
                instance = MoneySecureTransformMethod()
            }
            return instance!!
        }
    }
}