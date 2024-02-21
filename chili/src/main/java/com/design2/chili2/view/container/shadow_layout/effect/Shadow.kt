package com.design2.chili2.view.container.shadow_layout.effect

import android.graphics.*
import com.design2.chili2.view.container.shadow_layout.model.ShadowType
import com.design2.chili2.view.container.shadow_layout.utils.*

class Shadow : Effect {

    override val paint by lazy { Paint() }
    override val path by lazy { Path() }

    override var offsetLeft = 0f
    override var offsetTop = 0f
    override var offsetRight = 0f
    override var offsetBottom = 0f

    override var alpha = 0f

    var isBackgroundShadow = true
    private val isEnable: Boolean
        get() = blurSize != 0f && shadowColor != ViewHelper.NOT_SET_COLOR
    private var blurSize = 0f
    private var blurSizeOffset = 0f

    private var shadowColor = ViewHelper.NOT_SET_COLOR
    private var shadowOffsetX = 0f
    private var shadowOffsetY = 0f

    private var shadowType: ShadowType = ShadowType.SINGLE

    private var viewWidth: Float = 0f
    private var viewHeight: Float = 0f

    fun init(isBackground: Boolean, blurSize: Float, shadowOffsetX: Float, shadowOffsetY: Float, shadowColor: Int, shadowTypeInt: Int) {
        this.isBackgroundShadow = isBackground
        this.blurSize = blurSize
        this.blurSizeOffset = blurSize + blurSize
        this.shadowOffsetX = shadowOffsetX
        this.shadowOffsetY = shadowOffsetY
        this.shadowColor = shadowColor
        this.shadowType = when(shadowTypeInt) {
            ShadowType.TOP.value -> ShadowType.TOP
            ShadowType.MIDDLE.value -> ShadowType.MIDDLE
            ShadowType.BOTTOM.value -> ShadowType.BOTTOM
            else -> ShadowType.SINGLE
        }

        updatePaint()
    }

    override fun drawEffect(canvas: Canvas?) {

        if (!isEnable)
            return

        when (shadowType) {
            ShadowType.TOP -> {
                canvas?.clipRect(0f, 0f, viewWidth, viewHeight + blurSizeOffset, Region.Op.DIFFERENCE)
            }
            ShadowType.MIDDLE -> {
                canvas?.clipRect(0f, 0 - blurSizeOffset, viewWidth, 0f, Region.Op.DIFFERENCE)
                canvas?.clipRect(0f, viewHeight, viewWidth, viewHeight + blurSizeOffset, Region.Op.DIFFERENCE)
            }
            ShadowType.BOTTOM -> {
                canvas?.clipRect(0f, 0 - blurSizeOffset, viewWidth, 0f, Region.Op.DIFFERENCE)
            }
        }

        canvas?.drawPath(path, paint)
    }

    override fun updateOffset(left: Float, top: Float, right: Float, bottom: Float) {
        this.offsetLeft = left
        this.offsetTop = top
        this.offsetRight = right
        this.offsetBottom = bottom
    }

    override fun updatePaint() {

        paint.apply {
            isAntiAlias = true
            color = shadowColor

            if (Util.onSetAlphaFromColor(this@Shadow.alpha, shadowColor)) {
                alpha = Util.getIntAlpha(this@Shadow.alpha)
            }

            style = Paint.Style.FILL_AND_STROKE

            if (blurSize != 0f) {
                maskFilter = if (isBackgroundShadow) {
                    BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL)
                } else {
                    strokeWidth = blurSize
                    BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL)
                }
            }
        }
    }

    override fun updatePath(radiusInfo: Radius?) {

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

    override fun updateAlpha(alpha: Float) {
        this.alpha = alpha
        updatePaint()
    }

    override fun updateViewSize(width: Float, height: Float) {
        this.viewWidth = width
        this.viewHeight = height
    }

    fun getShadowColor(): Int = shadowColor
    fun getShadowBlurSize(): Float = blurSize
    fun getShadowOffsetX(): Float = shadowOffsetX
    fun getShadowOffsetY(): Float = shadowOffsetY

    fun updateShadowColor(color: Int) {
        this.shadowColor = color
        updatePaint()
    }

    fun updateShadowOffsetX(offset: Float) {
        this.shadowOffsetX = offset
    }

    fun updateShadowOffsetY(offset: Float) {
        this.shadowOffsetY = offset
    }
}