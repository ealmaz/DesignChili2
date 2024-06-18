package com.design2.chili2.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ChiliSwipeHorizontalRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var lastX = 0f
    private var lastY = 0f
    private val tiltThreshold = 5
    private var isScrollingHorizontally = false

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if (e.pointerCount > 1) return false
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = e.x
                lastY = e.y
                isScrollingHorizontally = false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = e.x - lastX
                val dy = e.y - lastY
                if (!isScrollingHorizontally) {
                    if (abs(dx) > abs(dy) && abs(dx) > tiltThreshold) {
                        // Scroll horizontally
                        isScrollingHorizontally = true
                        parent?.requestDisallowInterceptTouchEvent(true)
                        return true
                    } else if (abs(dy) > abs(dx) && abs(dy) > tiltThreshold) {
                        // Scroll vertically, let parent handle it
                        parent?.requestDisallowInterceptTouchEvent(false)
                        return false
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}
