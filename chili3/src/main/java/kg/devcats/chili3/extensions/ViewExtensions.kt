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
    onPressed: () -> Unit,
    onDefault: () -> Unit
) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onPressed()
                true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                onDefault()
                true
            }
            else -> false
        }
    }
}