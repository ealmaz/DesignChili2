package com.design2.chili2.view.camera_overlays

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.extensions.dp

class ForeignPassportOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.foreignPassportCameraOverlay,
    defStyleRes: Int = R.style.Chili_CameraOverlayRectangle
): BaseCameraOverlay(context, attrs, defStyleAttr, defStyleRes) {

    private var overlayAlpha = 100

    init {
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private var passportMaskRect: RectF? = null

    private var headerText: String? = null

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ForeignPassportOverlay, defStyleAttr, defStyleRes).run {
            overlayAlpha = getInteger(R.styleable.RectangleOverlay_overlayAlpha, 77)
            headerText = getString(R.styleable.ForeignPassportOverlay_headerText)
            recycle()
        }
    }

    fun setHeaderText(text: String) {
        headerText = text
        invalidate()
    }

    fun setHeaderText(@StringRes textRes: Int) {
        headerText = resources.getString(textRes)
        invalidate()
    }

    fun setOverlayAlpha(alpha: Int) {
        overlayAlpha = alpha
        invalidate()
    }

    fun getPassportMaskRectF(): RectF? {
        return passportMaskRect
    }

    override fun drawShapes(canvas: Canvas) {
        drawColor(canvas, R.color.black_1, overlayAlpha)
        var y = drawHeaderText(canvas, 0f)
        y = cutPassportCardShape(canvas, y)
    }

    private fun drawHeaderText(canvas: Canvas, startY: Float): Float {
        if (headerText == null) return startY
        val startYWithMargin = startY + 56.dp.toFloat()
        return drawText(
            canvas,
            headerText ?: "",
            (width / 2).toFloat(),
            startYWithMargin,
            TextConfig(16.dp.toFloat(), Typeface.BOLD)
        )
    }

    private fun cutPassportCardShape(canvas: Canvas, startY: Float): Float {
        val startYWithMargin = startY + 24.dp.toFloat()
        val marginPx = 16.dp

        val bottomMargin = 120.dp.toFloat()

        val rectWidth = canvas.width - (2 * marginPx)
        val rectHeight = canvas.height - startYWithMargin - bottomMargin

        val left = marginPx.toFloat()
        val right = left + rectWidth
        val bottom = startYWithMargin + rectHeight

        return cutRectangleFromCanvas(
            canvas,
            left,
            startYWithMargin,
            right,
            bottom
        )
    }
}