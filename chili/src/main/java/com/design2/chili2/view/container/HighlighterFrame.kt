package com.design2.chili2.view.container

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.design2.chili2.R
import kotlin.math.cos
import kotlin.math.sin

class HighlighterFrame @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.highlighterFrameDefaultStyle,
    defStyleRes: Int = R.style.Chili_HighlighterFrame,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val strokeWidth: Float
    private var gradientColors: IntArray
    private val cornerRadius: Float

    init {
        context.obtainStyledAttributes(attrs, R.styleable.HighlighterFrame, defStyleAttr, defStyleRes).run {

            getDimensionPixelSize(R.styleable.HighlighterFrame_cornerRadius, resources.getDimensionPixelSize(R.dimen.radius_16dp)).let {
                cornerRadius = it.toFloat()
            }
            getDimensionPixelSize(R.styleable.HighlighterFrame_strokeWidth, resources.getDimensionPixelSize(R.dimen.view_2dp)).let {
                strokeWidth = it.toFloat()
            }
            val startColor = getColor(R.styleable.HighlighterFrame_gradientStartColor, Color.TRANSPARENT)
            val endColor = getColor(R.styleable.HighlighterFrame_gradientEndColor, Color.TRANSPARENT)

            gradientColors = intArrayOf(startColor, endColor)

            recycle()
        }
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var angle = 0f
    private var animator: ValueAnimator? = null


    private val framePath: Path by lazy {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val innerRect = RectF(rect).apply { inset(strokeWidth, strokeWidth) }

        Path().apply {
            addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
            if (innerRect.width() > 0 && innerRect.height() > 0) {
                addRoundRect(innerRect, cornerRadius, cornerRadius, Path.Direction.CW)
            }
            fillType = Path.FillType.EVEN_ODD
        }
    }

    private val animationPaint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (animator?.isRunning != true)  return

        centerX = width / 2f
        centerY = height / 2f
        radius = (Math.min(width, height) / 2f) * 0.9f

        val startX = centerX + (radius * cos(Math.toRadians(angle.toDouble()))).toFloat()
        val startY = centerY + (radius * sin(Math.toRadians(angle.toDouble()))).toFloat()

        val endX = centerX + (radius * cos(Math.toRadians((angle + 180).toDouble()))).toFloat()
        val endY = centerY + (radius * sin(Math.toRadians((angle + 180).toDouble()))).toFloat()

        animationPaint.shader = LinearGradient(
            startX, startY, endX, endY,
            gradientColors, null, Shader.TileMode.CLAMP
        )
        canvas.drawPath(framePath, animationPaint)
    }

    fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 360f)
        animator?.duration = 3000
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener { valueAnimator ->
            angle = valueAnimator.animatedValue as Float
            invalidate()
        }

        animator?.start()
    }

    fun setGradientColors(@ColorInt colors: IntArray) {
        this.gradientColors = colors
    }

    fun stopAnimation() {
        animator?.cancel()
    }
}