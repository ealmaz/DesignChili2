package com.design2.chili2.view.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs

interface GesturesListener {
    fun onLongPressStart()
    fun onLongPressRelease()
    fun onTapLeft()
    fun onTapRight()
    fun onSwipeLeft()
    fun onSwipeRight()
    fun onSwipeUp(showDescription: Boolean)
    fun onSwipeDown(deltaY: Float)
    fun onSwipeDownEnd(isEnoughDragging: Boolean, isSwipeUp: Boolean)
}

class GesturesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), GestureDetector.OnGestureListener {

    private val gestureDetector: GestureDetector
    private var gestureListener: GesturesListener? = null

    private var startY: Float = 0f
    private var startX: Float = 0f
    private var deltaDownY: Float = 0f
    private var maxY: Float = 0f

    private var isDragging = false

    init {
        gestureDetector = GestureDetector(context, this)
        isClickable = true
    }

    fun setGestureListener(listener: GesturesListener) {
        this.gestureListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.y
                startX = event.x
                isDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (maxY < event.y) maxY = event.y

                val deltaY = event.y - startY
                val deltaX = event.x - startX
                deltaDownY = deltaY
                val isSwipeDown = deltaY > ZERO

                if (!isDragging && abs(deltaY) > VERTICAL_SWIPE_THRESHOLD && abs(deltaY) > abs(deltaX)) {
                    if (isSwipeDown) isDragging = true
                    else gestureListener?.onSwipeUp(true)
                }
                if (isDragging) {
                    if (!isSwipeDown) gestureListener?.onSwipeUp(false)
                    else gestureListener?.onSwipeDown(deltaY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val isEnoughDownDragging = (maxY - startY) >= ENOUGH_DRAGGING
                val isSwipeUp = deltaDownY < ZERO

                if (isDragging && isEnoughDownDragging) {
                    gestureListener?.onSwipeDownEnd(true, isSwipeUp)
                    isDragging = false
                } else
                    gestureListener?.onSwipeDownEnd(false, isSwipeUp)
            }
        }
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val tapedArea = width / 2
        if (e.x < tapedArea)
            gestureListener?.onTapLeft()
        else
            gestureListener?.onTapRight()
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean { return true }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean { return false }

    override fun onDown(e: MotionEvent): Boolean { return true }

    override fun onShowPress(e: MotionEvent) {}

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean { return false }

    override fun onLongPress(p0: MotionEvent?) {}

    companion object {
        const val VERTICAL_SWIPE_THRESHOLD = 50
        const val ENOUGH_DRAGGING = 65
        const val ZERO = 0
    }
}