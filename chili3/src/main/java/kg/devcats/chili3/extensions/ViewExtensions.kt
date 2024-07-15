package kg.devcats.chili3.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
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

@SuppressLint("ClickableViewAccessibility")
fun View.setSurfaceClick(
    onPressedState: () -> Unit,
    onDefaultState: () -> Unit
) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onPressedState()
                false
            }
            MotionEvent.ACTION_UP -> {
                onDefaultState()
                v.performClick()
                false
            }
            MotionEvent.ACTION_CANCEL -> {
                onDefaultState()
                false
            }
            else -> false
        }
    }
}