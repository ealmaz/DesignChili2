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

        val offset = 20
        val src = createBitmap(highlighterColor) {
            drawCircle(width - frameStrokeWith - offset , frameStrokeWith + offset, iconSizePx.toFloat() + offset, it)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).applyCanvas {
            dest?.let { drawBitmap(it, 0f, 0f, plainPaint) }
            src?.let { drawBitmap(it, 0f, 0f, iconBackgroundPaint) }
        }

        canvas?.drawBitmap(bitmap, 0f, 0f, plainPaint)
    }

    private fun drawIcon(canvas: Canvas) {
        val topOffset = 20
        highlighterIcon?.apply {
            setBounds(
                width - iconSizePx - frameStrokeWith.toInt() - topOffset,
                topOffset,
                width - frameStrokeWith.toInt() - topOffset,
                topOffset + iconSizePx
            )
            this.draw(canvas)
        }
    }

//    var rotationDeg = 0f
//
//    var isAnimStarted = false

//    private fun drawRectAnimation(canvas: Canvas?) {
//        prepareGradientPaintIfNeed()
//        framePaint.color = highlighterColor
//        val dest = createBitmap(Color.GREEN) {
//            drawRoundRect(
//                frameStrokeWith,
//                frameStrokeWith,
//                width - frameStrokeWith,
//                height - frameStrokeWith,
//                cornerRadiusPx,
//                cornerRadiusPx,
//                framePaint
//            )
//        }
//
//        val offset = width * 2
//
//        val src = createBitmap(Color.WHITE) {
//            drawArc(0f - offset, 0f - offset, width.toFloat() + offset, height.toFloat() + offset, rotationDeg, 80f, true, it)
//        }
//
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).applyCanvas {
//            drawBitmap(dest!!, 0f, 0f, plainPaint)
//            drawBitmap(src!!, 0f, 0f, Paint().apply {
//                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
//            })
//        }
//        canvas?.drawBitmap(bitmap, 0f, 0f, plainPaint)
//
//        if (isAnimStarted) return
//        rotationDeg = (rotationDeg + 4) % 360
//
//
//        val valueAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
//            repeatMode = ValueAnimator.RESTART
//            repeatCount = ValueAnimator.INFINITE
//            duration = 800
//            interpolator = AccelerateInterpolator()
//        }
//        valueAnimator.addUpdateListener {
//            rotationDeg = it.animatedValue as Float
//            invalidate()
//        }
//        isAnimStarted = true
//        valueAnimator.start()
//    }


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