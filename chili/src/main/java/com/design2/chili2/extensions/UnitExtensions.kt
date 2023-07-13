package com.design2.chili2.extensions

import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Int.dpF: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)