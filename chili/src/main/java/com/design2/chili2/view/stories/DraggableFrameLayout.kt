package com.design2.chili2.view.stories

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

class DraggableFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var dragHelper: ViewDragHelper? = null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        dragHelper?.let {
            it.processTouchEvent(ev)
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        dragHelper?.let {
            it.processTouchEvent(ev)
        }
        return super.onTouchEvent(ev)
    }
}
