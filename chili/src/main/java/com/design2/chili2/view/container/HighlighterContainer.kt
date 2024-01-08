package com.design2.chili2.view.container

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.toColorInt
import com.design2.chili2.R
import com.design2.chili2.util.HighlighterState
import com.design2.chili2.util.toHighlighterStateEnum

class HighlighterContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.highlighterContainerDefaultStyle,
    defStyleRes: Int = R.style.Chili_Container_HighlighterContainer
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var cornerRadiusPx: Float = 0f
    private var highlighterIcon: Drawable? = null
    private var highlighterState: HighlighterState? = null

    private val px3 = resources.getDimensionPixelSize(R.dimen.view_3dp).toFloat()
    private val px5 = resources.getDimensionPixelSize(R.dimen.view_5dp).toFloat()
    private val px12 = resources.getDimensionPixelSize(R.dimen.view_12dp).toFloat()
    private val px15 = resources.getDimensionPixelSize(R.dimen.view_15dp).toFloat()
    private val px18 = resources.getDimensionPixelSize(R.dimen.view_18dp).toFloat()
    private val px21 = resources.getDimensionPixelSize(R.dimen.view_21dp).toFloat()

    private var iconSizePx: Int = 0

    @ColorInt
    private var highlighterColor: Int = Color.GREEN
    @ColorInt
    private var highlighterGradientStartColor: Int? = null
    @ColorInt
    private var highlighterGradientEndColor: Int? = null


    private val frameStrokeWith = resources.getDimensionPixelSize(R.dimen.view_2dp).toFloat()


    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = frameStrokeWith
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    private val plainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    private val iconBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    init {
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.HighlighterContainer,
            defStyleAttr,
            defStyleRes
        ).run {
            cornerRadiusPx =
                getDimensionPixelSize(R.styleable.HighlighterContainer_cornerRadius, 0).toFloat()
            highlighterIcon = getDrawable(R.styleable.HighlighterContainer_highlighterIcon)
            highlighterState = getInteger(
                R.styleable.HighlighterContainer_highlighterState,
                0
            ).toHighlighterStateEnum()
            highlighterColor =
                getColor(R.styleable.HighlighterContainer_highlighterColor, Color.GREEN)
            highlighterGradientStartColor = getColor(
                R.styleable.HighlighterContainer_highlighterGradientStartColor,
                -1
            ).takeIf { it != -1 }
            highlighterGradientEndColor = getColor(
                R.styleable.HighlighterContainer_highlighterGradientEndColor,
                -1
            ).takeIf { it != -1 }
            iconSizePx =
                getDimensionPixelSize(R.styleable.HighlighterContainer_highlighterIconSize, 0)
            recycle()
        }
    }

    fun setHighlighterState(highlighterState: HighlighterState) {
        this.highlighterState = highlighterState
        invalidate()
    }

    fun setHighlighterColor(@ColorInt color: Int) {
        this.highlighterColor = color
        framePaint.color = color
        highlighterGradientStartColor = null
        highlighterGradientEndColor = null
        invalidate()
    }

    fun setHighlighterGradientColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        highlighterGradientStartColor = startColor
        highlighterGradientEndColor = endColor
        invalidate()
    }

    fun setHighlighterGradientColors(colors: List<String>?) {
        colors?.get(0)?.let { highlighterGradientStartColor = it.toColorInt() }
        highlighterGradientEndColor = when (colors?.size) {
            2 -> colors[1].toColorInt()
            1 -> colors[0].toColorInt()
            else -> null
        }
        invalidate()
    }

    fun setHighlighterIcon(icon: Drawable) {
        this.highlighterIcon = icon
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        when (highlighterState) {
            HighlighterState.VISIBLE -> drawFrame(canvas)
            HighlighterState.WITH_ICON -> {
                drawFrame(canvas)
                drawIconBackground(canvas)
                canvas?.let { drawIcon(it) }
            }
        }
    }

    private fun drawFrame(canvas: Canvas?) {
        framePaint.color = highlighterColor
        prepareGradientPaintIfNeed()
        canvas?.drawRoundRect(
            frameStrokeWith,
            frameStrokeWith,
            width - frameStrokeWith,
            height - frameStrokeWith,
            cornerRadiusPx,
            cornerRadiusPx,
            framePaint
        )
    }

    private fun prepareGradientPaintIfNeed() {
        if (highlighterGradientStartColor == null || highlighterGradientEndColor == null) return
        framePaint.shader = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            highlighterGradientStartColor!!,
            highlighterGradientEndColor!!,
            Shader.TileMode.MIRROR
        )
    }

    private fun drawIconBackground(canvas: Canvas?) {
        val dest = createBitmap(Color.GREEN) {
            drawRoundRect(
                frameStrokeWith,
                frameStrokeWith,
                width - frameStrokeWith,
                height - frameStrokeWith,
                cornerRadiusPx,
                cornerRadiusPx,
                it
            )
        }
        val src = createBitmap(highlighterColor) {
            drawCircle(width - px21 + px12, 0 + px21 - px12, px12, it)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).applyCanvas {
            dest?.let { drawBitmap(it, 0f, 0f, plainPaint) }
            src?.let { drawBitmap(it, 0f, 0f, iconBackgroundPaint) }
        }

        canvas?.drawBitmap(bitmap, 0f, 0f, plainPaint)
    }

    private fun drawIcon(canvas: Canvas) {
     highlighterIcon?.apply {
            setBounds(
                (width - px15 - frameStrokeWith).toInt(),
                (px3 + frameStrokeWith).toInt(),
                (width - px5 - frameStrokeWith).toInt(),
                (px18).toInt()
            )
            this.draw(canvas)
        }
    }


    private fun createBitmap(color: Int, drawing: Canvas.(Paint) -> Unit): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
        bitmap.applyCanvas {
            drawing.invoke(this, paint)
        }
        return bitmap
    }
}