package kg.devcats.chili3.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.Motion

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
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onPressed()
                true
            }
            MotionEvent.ACTION_UP -> {
                onDefault()
                v.performClick()
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                onDefault()
                true
            }
            else -> false
        }
    }
}