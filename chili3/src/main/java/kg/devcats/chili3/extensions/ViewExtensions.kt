package kg.devcats.chili3.extensions

import android.animation.AnimatorInflater
import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources

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
                true
            }
            MotionEvent.ACTION_UP -> {
                onDefaultState()
                v.performClick()
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                onDefaultState()
                true
            }
            else -> false
        }
    }
}

fun View.applyStateListAnimatorFromTheme(context: Context, attrResId: Int) {
    val typedValue = TypedValue()
    val theme = context.theme

    if (theme.resolveAttribute(attrResId, typedValue, true)) {
        val stateListAnimatorResId = typedValue.resourceId
        if (stateListAnimatorResId != 0) {
            val stateListAnimator: StateListAnimator =
                AnimatorInflater.loadStateListAnimator(context, stateListAnimatorResId)
            this.stateListAnimator = stateListAnimator
        }
    }
}

fun View.applyForegroundFromTheme(context: Context, attrResId: Int) {
    val typedValue = TypedValue()
    val theme = context.theme

    if (theme.resolveAttribute(attrResId, typedValue, true)) {
        val foregroundDrawableResId = typedValue.resourceId
        if (foregroundDrawableResId != 0) {
            val foregroundDrawable: Drawable? =
                AppCompatResources.getDrawable(context,foregroundDrawableResId)
            foreground = foregroundDrawable
        }
    }
}