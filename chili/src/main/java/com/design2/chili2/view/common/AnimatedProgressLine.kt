package com.design2.chili2.view.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.design2.chili2.R

interface ProgressListener {
    fun onLineProgressFull()
}

class AnimatedProgressLine(context: Context, private val attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private var animator: ValueAnimator? = null
    private var progressListener: ProgressListener? = null

    @ColorInt private var progressBackgroundColor: Int = Color.GRAY
    @ColorInt private var progressColor: Int = Color.GREEN

    private var progressPercent: Int = 0
    private var animationDuration: Long = 1000
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
            getColor(R.styleable.AnimatedProgressLine_progressBackgroundColor, Color.GRAY).let {
                setProgressBackgroundColor(it)
            }
            getInteger(R.styleable.AnimatedProgressLine_progressPercent, 50).let {
                setProgress(it)
            }
            getInteger(R.styleable.AnimatedProgressLine_animationDuration, 1000).let {
                setAnimationDuration(it.toLong())
            }
            recycle()
        }
    }


    fun setProgressColor(@ColorInt color: Int) {
        this.progressColor = color
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        this.progressBackgroundColor = color
        invalidate()
    }

    fun setProgressPercent(percent: Int) {
        this.progressPercent = percent
        if (percent == 100) progressListener?.onLineProgressFull()
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawBackground(canvas)
        drawProgress(canvas)

    }

    private fun drawBackground(canvas: Canvas?) {
        if (canvas == null) return
        paint.apply {
            color = progressBackgroundColor
            strokeWidth = height.toFloat()
        }
        val roundOffset = height / 2f
        canvas.drawLine(roundOffset, roundOffset, (width - roundOffset), roundOffset, paint)
    }

    private fun drawProgress(canvas: Canvas?) {
        if (canvas == null || progressPercent <= 0) return
        paint.apply {
            color = progressColor
            strokeWidth = height.toFloat()
        }
        val progress = (width / 100f * progressPercent).coerceAtLeast(height.toFloat())
        val roundOffset = height / 2f
        canvas.drawLine(roundOffset, roundOffset, (progress - roundOffset), roundOffset, paint)
    }

    private fun animateProgress(progress: Int) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, progress).apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            addUpdateListener { setProgressPercent(it.animatedValue as Int) }
            start()
        }
    }

    private fun reverseAnimateProgress() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(progressPercent, 0).apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            addUpdateListener { setProgressPercent(it.animatedValue as Int) }
            start()
        }
    }

    fun setIsProgressAnimated(isAnimated: Boolean) {
        this.isProgressAnimated = isAnimated
        if (!isAnimated) {
            animator?.cancel()
        }
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

    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
        invalidate()
    }

    fun pauseAnimation() {
        animator?.pause()
    }

    fun resumeAnimation() {
        animator?.resume()
    }

    fun cancelAnimation() {
        animator?.cancel()
    }

    fun setAnimationListener(listener: ProgressListener) {
        this.progressListener = listener
    }
}