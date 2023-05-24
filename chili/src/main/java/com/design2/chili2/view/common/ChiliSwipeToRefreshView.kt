package com.design2.chili2.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.design2.chili2.R
import com.design2.chili2.extensions.getColorFromAttr
import kotlin.math.abs

class ChiliSwipeToRefreshView(context: Context, attributeSet: AttributeSet) :
    SwipeRefreshLayout(context, attributeSet) {

    private var currentDownX: Float = 0F
    private var currentDownY: Float = 0F

    init {
        setColorSchemeColors(context.getColorFromAttr(R.attr.ChiliSwipeToRefreshViewColor))
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val onInterceptTouchEvent = super.onInterceptTouchEvent(ev)
        if ((ev?.pointerCount ?: 0) > 1) return false
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                currentDownX = ev.x
                currentDownY = ev.y
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                return if (abs(ev.y - currentDownY) / abs(ev.x - currentDownX) > 10f) onInterceptTouchEvent else false
            }
        }
        return onInterceptTouchEvent
    }
}