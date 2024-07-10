package kg.devcats.chili3.extensions

import android.view.View

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}