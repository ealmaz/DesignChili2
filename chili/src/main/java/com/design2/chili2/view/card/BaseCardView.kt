package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import com.design2.chili2.R

abstract class BaseCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * WARNING: Call `initView` function in init block
     */
    fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        inflateView(context)
        setupView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    abstract val styleableAttrRes: IntArray

    abstract fun inflateView(context: Context)

    protected fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, styleableAttrRes, defStyleAttr, defStyleRes).run {
            obtainAttributes()
            recycle()
        }
    }

    open fun TypedArray.obtainAttributes() {}

    open fun setupView() {}
}