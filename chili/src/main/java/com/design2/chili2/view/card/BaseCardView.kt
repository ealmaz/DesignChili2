package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.design2.chili2.R
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

abstract class BaseCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle
) : CardView(context, attrs, defStyleAttr), ShimmeringView {

    protected val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    /**
     * WARNING: Call `initView` function in init block
     * create init block after all variable declarations
     */
    fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        inflateView(context)
        setupView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupShimmeringViews()
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
            foreground = getDrawable(R.styleable.BaseCardView_android_foreground)
            isClickable = getBoolean(R.styleable.BaseCardView_android_clickable, false)
            isFocusable = getBoolean(R.styleable.BaseCardView_android_focusable, false)
            recycle()
        }

        context.obtainStyledAttributes(attrs, styleableAttrRes, defStyleAttr, defStyleRes).run {
            obtainAttributes()
            recycle()
        }
    }

    override fun getShimmeringViewsPair() = shimmeringPairs

    open fun setCardBackground(resId: Int) {
        rootContainer?.setBackgroundResource(resId)
    }

    open fun setCardBackground(drawable: Drawable) {
        rootContainer?.background = drawable
    }

    open fun TypedArray.obtainAttributes() {}

    open fun setupView() {
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    open fun setupShimmeringViews() {}
}