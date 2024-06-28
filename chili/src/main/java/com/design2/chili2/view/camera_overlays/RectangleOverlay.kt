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

    private var description: String? = null

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.RectangleOverlay, defStyleAttr, defStyleRes).run {
            overlayAlpha = getInteger(R.styleable.RectangleOverlay_overlayAlpha, 77)
            description = getString(R.styleable.PassportCardOverlay_description)
            recycle()
        }
    }

    fun setDescription(text: String) {
        description = text
        invalidate()
    }

    fun setOverlayAlpha(alpha: Int) {
        overlayAlpha = alpha
        invalidate()
    }

    override fun drawShapes(canvas: Canvas) {
        drawColor(canvas, R.color.black_1, overlayAlpha)
        val y = cutPassportCardShape(canvas)
        drawDescription(canvas, y)
    }

    private fun drawDescription(canvas: Canvas, startY: Float): Float {
        if (description == null) return startY
        val startYWithMargin = startY + 24.dp.toFloat()
        return drawText(
            canvas,
            description ?: "",
            (width / 2).toFloat(),
            startYWithMargin,
            TextConfig(16.dp.toFloat())
        )
    }

    private fun cutPassportCardShape(canvas: Canvas): Float {
        val startYWithMargin = 72.dp.toFloat()
        val bottomMargin = if (description == null) 120.dp.toFloat() else 178.dp.toFloat()
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