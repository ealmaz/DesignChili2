package com.design2.chili2.view.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

interface GesturesListener {
    fun onLongPressStart()
    fun onLongPressRelease()
    fun onTapLeft()
    fun onTapRight()
    fun onSwipeLeft()
    fun onSwipeRight()
    fun onSwipeUp()
    fun onSwipeDown()
}

class GesturesView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint()
    private val gestureDetector: GestureDetectorCompat
    private var gestureListener: GesturesListener? = null
    private var startX = 0f
    private var startY = 0f
    private var isLongPressing = false

    init {
        paint.color = Color.TRANSPARENT
        gestureDetector = GestureDetectorCompat(context, GestureListener())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun setGestureListener(listener: GesturesListener) {
        this.gestureListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                isLongPressing = false
            }
            MotionEvent.ACTION_UP -> {
                if (isLongPressing) {
                    gestureListener?.onLongPressRelease()
                    isLongPressing = false
                } else {
                    checkTapAndSwipe(event)
                }
            }
        }

        return true
    }

    private fun checkTapAndSwipe(event: MotionEvent) {
        val deltaX = event.x - startX
        val deltaY = event.y - startY

        if (abs(deltaX) < TAP_THRESHOLD && abs(deltaY) < TAP_THRESHOLD) {
            if (event.x < width / 2) {
                gestureListener?.onTapLeft()
            } else {
                gestureListener?.onTapRight()
            }
        } else {
            if (abs(deltaX) > abs(deltaY)) {
                if (deltaX > 0) {
                    gestureListener?.onSwipeRight()
                } else {
                    gestureListener?.onSwipeLeft()
                }
            } else {
                if (deltaY > 0) {
                    gestureListener?.onSwipeDown()
                } else {
                    gestureListener?.onSwipeUp()
                }
            }
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            isLongPressing = true
            gestureListener?.onLongPressStart()
        }
    }

    companion object {
        private const val TAP_THRESHOLD = 10
    }
}