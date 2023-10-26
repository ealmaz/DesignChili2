package com.design2.chili2.view.camera_overlays

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.design2.chili2.R
import com.design2.chili2.extensions.dp

class RectangleOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.rectangleCameraOverlay,
    defStyleRes: Int = R.style.Chili_CameraOverlayRectangle
): BaseCameraOverlay(context, attrs, defStyleAttr, defStyleRes) {

    private var overlayAlpha = 100

    init {
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.RectangleOverlay, defStyleAttr, defStyleRes).run {
            overlayAlpha = getInteger(R.styleable.RectangleOverlay_overlayAlpha, 77)
            recycle()
        }
    }

    fun setOverlayAlpha(alpha: Int) {
        overlayAlpha = alpha
        invalidate()
    }

    override fun drawShapes(canvas: Canvas) {
        drawColor(canvas, R.color.black_1, overlayAlpha)
        cutPassportCardShape(canvas)
    }

    private fun cutPassportCardShape(canvas: Canvas): Float {
        val startYWithMargin = 72.dp.toFloat()
        val bottomMargin = 120.dp.toFloat()
        val marginPx = 16.dp

        val rectWidth = canvas.width - (2 * marginPx)
        val rectHeight = canvas.height - startYWithMargin - bottomMargin

        val left = marginPx.toFloat()
        var top = startYWithMargin
        val right = left + rectWidth
        val bottom = top + rectHeight

        return cutRectangleFromCanvas(
            canvas,
            left,
            top,
            right,
            bottom
        )
    }
}