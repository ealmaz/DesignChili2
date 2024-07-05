package com.design2.chili2.view.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.design2.chili2.R

class AnimatedProgressLine(context: Context, private val attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()

    @ColorInt private var progressBackgroundColor: Int = Color.GRAY
    @ColorInt private var progressColor: Int = Color.GREEN
    @ColorInt private var progressGradientStartColor: Int? = null
    @ColorInt private var progressGradientEndColor: Int? = null

    private var progressPercent: Int = 0
    private var isProgressAnimated: Boolean = false

    init {
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        initViews()
    }

    private fun initViews() {
        context?.obtainStyledAttributes(attrs, R.styleable.AnimatedProgressLine)?.run {
            getBoolean(R.styleable.AnimatedProgressLine_animateProgress, false).let {
                setIsProgressAnimated(it)
            }
            getColor(R.styleable.AnimatedProgressLine_progressColor, Color.GREEN).let {
                setProgressColor(it)
            }
            getColor(R.styleable.AnimatedProgressLine_progressGradientStartColor, -1)
                .takeIf { it != -1 }.let { progressGradientStartColor = it }
            getColor(R.styleable.AnimatedProgressLine_progressGradientEndColor, -1)
                .takeIf { it != -1 }.let { progressGradientEndColor = it }
            getColor(R.styleable.AnimatedProgressLine_progressBackgroundColor, Color.GRAY).let {
                setProgressBackgroundColor(it)
            }
            getInteger(R.styleable.AnimatedProgressLine_progressPercent, 50).let {
                setProgress(it)
            }
            recycle()
        }
    }


    fun setProgressColor(@ColorInt color: Int) {
        this.progressColor = color
        invalidate()
    }

    fun setProgressGradientStartColor(@ColorInt startColor: Int) {
        this.progressGradientStartColor = startColor
        invalidate()
    }

    fun setProgressGradientEndColor(@ColorInt endColor: Int) {
        this.progressGradientEndColor = endColor
        invalidate()
    }

    fun setProgressGradientColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        this.progressGradientStartColor = startColor
        this.progressGradientEndColor = endColor
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        this.progressBackgroundColor = color
        invalidate()
    }

    private fun setProgressPercent(percent: Int) {
        this.progressPercent = percent
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawBackground(canvas)
        drawProgress(canvas)

    }

    private fun drawBackground(canvas: Canvas) {
        if (canvas == null) return
        paint.apply {
            color = progressBackgroundColor
            strokeWidth = height.toFloat()
        }
        val roundOffset = height / 2f
        canvas.drawLine(roundOffset, roundOffset, (width - roundOffset), roundOffset, paint)
    }

    private fun drawProgress(canvas: Canvas) {
        if (canvas == null || progressPercent <= 0) return
        paint.apply {
            color = progressColor
            strokeWidth = height.toFloat()
        }
        val progress = (width / 100f * progressPercent).coerceAtLeast(height.toFloat())
        prepareGradientPaintIfNeed(progress)
        val roundOffset = height / 2f
        canvas.drawLine(roundOffset, roundOffset, (progress - roundOffset), roundOffset, paint)
    }

    private fun prepareGradientPaintIfNeed(width: Float) {
        if (progressGradientStartColor == null || progressGradientEndColor == null) return
        paint.shader = LinearGradient(
            0f,
            0f,
            width,
            height.toFloat(),
            progressGradientStartColor!!,
            progressGradientEndColor!!,
            Shader.TileMode.MIRROR
        )
    }

    private fun animateProgress(progress: Int) {
        ValueAnimator.ofInt(0, progress).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener { setProgressPercent(it.animatedValue as Int) }
            start()
        }
    }

    private fun reverseAnimateProgress() {
        ValueAnimator.ofInt(progressPercent, 0).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener { setProgressPercent(it.animatedValue as Int) }
            start()
        }
    }

    fun setIsProgressAnimated(isAnimated: Boolean) {
        this.isProgressAnimated = isAnimated
    }

    fun setProgress(progress: Int) {
        if (!isProgressAnimated) {
            setProgressPercent(progress)
            return
        }
        if (progress < progressPercent) {
              reverseAnimateProgress()
        }
        animateProgress(progress)
    }
}