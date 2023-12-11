package com.design2.chili2.view.story

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.design2.chili2.R
import com.design2.chili2.view.common.AnimatedProgressLine
import com.design2.chili2.view.common.ProgressListener

interface ProgressBarListener {
    fun onAnimationFinishedAt(index: Int)
}

class StoryProgressBar @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet,
    defStyleAttr: Int = R.attr.progressCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_ProgressCellView
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), ProgressListener {

    private val animatedProgressLines = mutableListOf<AnimatedProgressLine>()
    private var progressListener: ProgressBarListener? = null

    private var _currentIndex: Int = 0
    val currentIndex: Int
        get() = _currentIndex

    private var barsHeight: Int = 0
    private var gapBetweenBars: Int = 0
    private var animationDuration: Long = 10000
    @ColorInt private var progressBackgroundColor: Int = Color.GRAY
    @ColorInt private var progressColor: Int = Color.WHITE

    init {
        orientation = HORIZONTAL
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.StoryProgressBar, defStyleAttr, defStyleRes).run {
            getColor(R.styleable.StoryProgressBar_progressColor, -1).takeIf { it != -1 }?.let {
                setProgressColor(it)
            }
            getColor(R.styleable.StoryProgressBar_progressBackgroundColor, -1).takeIf { it != -1 }?.let {
                setProgressBackgroundColor(it)
            }
            getDimensionPixelSize(R.styleable.StoryProgressBar_barsHeight, -1).takeIf { it != -1 }?.let {
                setBarsHeightInPx(it)
            }
            getDimensionPixelSize(R.styleable.StoryProgressBar_gapBetweenBars, -1).takeIf { it != -1 }?.let {
                setGapBetweenBarsInPx(it)
            }
            getInteger(R.styleable.StoryProgressBar_animationDuration, 10000).let {
                setAnimationDuration(it * 1000L)
            }
            getInteger(R.styleable.StoryProgressBar_progressBarCount, 1).takeIf { it != 1 }?.let {
                initProgressBar(it)
            }
            recycle()
        }
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressColor = color
        for (animatedProgressLine in animatedProgressLines) {
            animatedProgressLine.setProgressColor(color)
        }
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        progressBackgroundColor = color
        for (animatedProgressLine in animatedProgressLines) {
            animatedProgressLine.setProgressBackgroundColor(color)
        }
        invalidate()
    }

    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
        for (animatedProgressLine in animatedProgressLines) {
            animatedProgressLine.setAnimationDuration(duration)
        }
        invalidate()
    }

    private fun setBarsHeightInPx(height: Int) {
        barsHeight = height
        invalidate()
    }

    private fun setGapBetweenBarsInPx(gap: Int) {
        gapBetweenBars = gap
        invalidate()
    }

    fun initProgressBar(count: Int) {
        animatedProgressLines.clear()
        removeAllViews()

        for (i in 0 until count) {
            val animatedProgressLine = getProgressLine()
            val layoutParams = LayoutParams(0, barsHeight, 1f).apply {
                marginStart = if (i > 0) gapBetweenBars else 0
            }
            animatedProgressLines.add(animatedProgressLine)
            addView(animatedProgressLine, layoutParams)
        }
        invalidate()
        startAnimation()
    }

    private fun getProgressLine() =
        AnimatedProgressLine(context).apply {
            setProgressPercent(0)
            setIsProgressAnimated(true)
            setAnimationDuration(animationDuration)
            setAnimationListener(this@StoryProgressBar)
            setProgressColor(progressColor)
            setProgressBackgroundColor(progressBackgroundColor)
        }

    fun updateCurrentIndex(index: Int) {
        _currentIndex = index
    }

    fun startAnimation() {
        animatedProgressLines[currentIndex].cancelAnimation()
        animatedProgressLines[currentIndex].setProgress(100)
    }

    fun completeAnimation() {
        animatedProgressLines[currentIndex].cancelAnimation()
        animatedProgressLines[currentIndex].setProgressPercent(100)
    }

    fun resetAnimation() {
        animatedProgressLines[currentIndex].cancelAnimation()
        animatedProgressLines[currentIndex].setProgressPercent(0)
    }

    fun pauseAnimation() {
        animatedProgressLines[currentIndex].pauseAnimation()
    }

    fun resumeAnimation() {
        animatedProgressLines[currentIndex].resumeAnimation()
    }

    override fun onLineProgressFull() {
        progressListener?.onAnimationFinishedAt(currentIndex)
        if (currentIndex + 1 < animatedProgressLines.size) {
            updateCurrentIndex(currentIndex + 1)
            startAnimation()
        }
    }

    fun setProgressBarListener(listener: ProgressBarListener) {
        this.progressListener = listener
    }
}