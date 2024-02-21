package com.design2.chili2.view.container.shadow_layout.effect

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.design2.chili2.view.container.shadow_layout.utils.Util
import com.design2.chili2.view.container.shadow_layout.utils.ViewHelper

class Background : Effect {

    override val paint by lazy { Paint() }
    override val path by lazy { Path() }
    private val strokePaint by lazy { Paint() }
    private val strokePath by lazy { Path() }

    override var offsetLeft = 0f
    override var offsetTop = 0f
    override var offsetRight = 0f
    override var offsetBottom = 0f

    override var alpha = 0f

    private var backgroundColor = ViewHelper.NOT_SET_COLOR
    private var strokeInfo: Stroke? = null
    private var shadowInfo: Shadow? = null

    fun init(strokeInfo: Stroke?, backgroundColor: Int) {
        this.strokeInfo = strokeInfo
        this.backgroundColor = backgroundColor

        updatePaint()
    }

    override fun updateOffset(left: Float, top: Float, right: Float, bottom: Float) {
        this.offsetLeft = left
        this.offsetTop = top
        this.offsetRight = right
        this.offsetBottom = bottom
    }

    override fun updatePaint() {

        if (strokeInfo?.isEnable == true) {

            strokePaint.apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = strokeInfo!!.strokeWidth
                color = strokeInfo!!.strokeColor

                if (Util.onSetAlphaFromColor(this@Background.alpha, strokeInfo!!.strokeColor)) {
                    alpha = Util.getIntAlpha(this@Background.alpha)
                }

                if (strokeInfo?.gradient?.isEnable == true) {
                    shader = strokeInfo?.gradient?.getGradientShader()
                }
            }
        }

        paint.apply {
            isAntiAlias = true
            color = backgroundColor
            style = Paint.Style.FILL_AND_STROKE

            if (Util.onSetAlphaFromColor(this@Background.alpha, backgroundColor)) {
                alpha = Util.getIntAlpha(this@Background.alpha)
            }
        }
    }

    override fun updatePath(radiusInfo: Radius?) {

        if (strokeInfo?.isEnable == true) {

            val adjustOffset = strokeInfo!!.strokeWidth.div(2f).toInt()
            val strokeRect = RectF(
                offsetLeft - adjustOffset,
                offsetTop - adjustOffset,
                offsetRight + adjustOffset,
                offsetBottom + adjustOffset
            )

            strokePath.apply {
                reset()

                if (radiusInfo == null) {
                    addRect(strokeRect, Path.Direction.CW)
                } else {
                    val height = (offsetBottom - offsetTop).toInt()
                    addRoundRect(strokeRect, radiusInfo.getRadiusArray(height), Path.Direction.CW)
                }

                close()
            }
        }

        val rect = RectF(offsetLeft, offsetTop, offsetRight, offsetBottom)

        path.apply {

            reset()

            if (radiusInfo == null) {
                addRect(rect, Path.Direction.CW)
            } else {
                val height = (offsetBottom - offsetTop).toInt()
                addRoundRect(rect, radiusInfo.getRadiusArray(height), Path.Direction.CW)
            }

            close()
        }
    }

    override fun drawEffect(canvas: Canvas?) {

        if (strokeInfo?.isEnable == true) {
            canvas?.drawPath(strokePath, strokePaint)
        }

        canvas?.drawPath(path, paint)
    }

    override fun updateAlpha(alpha: Float) {
        this.alpha = alpha
        updatePaint()
    }

    override fun updateViewSize(width: Float, height: Float) {}


    fun setBackgroundColor(color: Int) {
        this.backgroundColor = color
        updatePaint()
    }

    fun updateStrokeWidth(strokeWidth: Float) {

        if (strokeInfo == null) {
            strokeInfo = Stroke()
        }

        strokeInfo!!.strokeWidth = strokeWidth
        updatePaint()
    }

    fun updateStrokeColor(color: Int) {
        if (strokeInfo == null) {
            strokeInfo = Stroke()
        }

        strokeInfo!!.strokeColor = color
        updatePaint()
    }

    fun getStrokeInfo(): Stroke? {
        return null
    }
}