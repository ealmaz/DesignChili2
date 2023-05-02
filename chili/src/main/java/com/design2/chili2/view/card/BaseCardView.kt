package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.design2.chili2.R

abstract class BaseCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle
) : CardView(context, attrs, defStyleAttr) {

    /**
     * WARNING: Call `initView` function in init block
     * create init block after all variable declarations
     */
    fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        inflateView(context)
        setupView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    abstract val styleableAttrRes: IntArray
    open val rootContainer: View? = null

    abstract fun inflateView(context: Context)

    protected fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.BaseCardView, defStyleAttr, defStyleRes).run {
            radius = getDimension(R.styleable.BaseCardView_cardCornerRadius, 0f)
            cardElevation = getDimension(R.styleable.BaseCardView_cardElevation, 0f)
            getResourceId(R.styleable.BaseCardView_cardBackground, -1)
                .takeIf { it != -1 }?.let { setCardBackground(it) }
            recycle()
        }

        context.obtainStyledAttributes(attrs, styleableAttrRes, defStyleAttr, defStyleRes).run {
            obtainAttributes()
            recycle()
        }
    }

    fun setCardBackground(resId: Int) {
        rootContainer?.setBackgroundResource(resId)
    }

    fun setCardBackground(drawable: Drawable) {
        rootContainer?.background = drawable
    }

    open fun TypedArray.obtainAttributes() {}

    open fun setupView() {}
}