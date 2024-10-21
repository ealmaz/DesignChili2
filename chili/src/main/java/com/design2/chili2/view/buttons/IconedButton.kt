package com.design2.chili2.view.buttons

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewButtonIconedBinding
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

class IconedButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.loaderButtonDefaultStyle,
    defStyle: Int = R.style.Chili_ButtonStyle
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle), ShimmeringView {

    private lateinit var vb: ChiliViewButtonIconedBinding
    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()
    private var isContentInvisibleOnShimmering = false
    private var isShimmeringStart = false
    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
        setupShimmeringViews()
    }

    private fun setupShimmeringViews() {
        if (isContentInvisibleOnShimmering) shimmeringPairs[vb.tvTitle] = null
    }

    private fun initView(context: Context) {
        vb = ChiliViewButtonIconedBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyle: Int
    ) {
        context.obtainStyledAttributes(
            attributeSet,
            R.styleable.IconedButton,
            defStyleAttr,
            defStyle
        ).run {
            isContentInvisibleOnShimmering = getBoolean(R.styleable.IconedButton_isContentInvisibleOnShimmering, false)
            setStartIcon(
                getResourceId(R.styleable.IconedButton_startIcon, -1).takeIf { it != -1 }
            )
            setupStartIconSize(
                widthPx = getDimensionPixelSize(R.styleable.IconedButton_startIconWidth, -1).takeIf { it != -1 },
                heightPx = getDimensionPixelSize(R.styleable.IconedButton_startIconHeight, -1).takeIf { it != -1 }
            )
            setText(getString(R.styleable.IconedButton_android_text))
            setEnabled(getBoolean(R.styleable.IconedButton_android_enabled, true))
            getResourceId(R.styleable.IconedButton_android_textAppearance, -1).takeIf { it != -1 }
                ?.let { setTextAppearance(it) }
            recycle()
        }
    }

    fun setStartIcon(url: String) {
        vb.ivStartIcon.apply {
            visible()
            setImageByUrl(url)
        }
    }

    fun setStartIcon(@DrawableRes drawableRes: Int?) = with(vb.ivStartIcon) {
        if (isContentInvisibleOnShimmering) {
            if (drawableRes == null) shimmeringPairs.remove(this)
            else shimmeringPairs[this] = null
        }
        setImageOrHide(drawableRes)
        if (isShimmeringStart && drawableRes != null) invisible()
    }

    private fun setupStartIconSize(widthPx: Int?, heightPx: Int?) {
        vb.ivStartIcon.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
        }
    }

    fun setStartIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupStartIconSize(widthPx, heightPx)
    }

    fun setText(text: String?) {
        vb.tvTitle.text = text
    }

    fun setText(@StringRes textResId: Int) {
        vb.tvTitle.setText(textResId)
    }

    fun setIcon(string: String) {
        vb.ivIcon.apply {
            visible()
            setImageByUrl(string)
        }
    }

    fun setIcon(@DrawableRes drawableRes: Int?) = with(vb.ivIcon) {
        if (isContentInvisibleOnShimmering) {
            if (drawableRes == null) shimmeringPairs.remove(this)
            else shimmeringPairs[this] = null
        }
        setImageOrHide(drawableRes)
        if (isShimmeringStart && drawableRes != null) invisible()
    }

    fun setTextColor(@ColorInt color: Int) {
        vb.tvTitle.setTextColor(color)
    }

    fun setTextStyle(typeface: Typeface) {
        vb.tvTitle.typeface = typeface
    }

    fun setTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    override fun onStartShimmer() {
        isShimmeringStart = true
    }

    override fun onStopShimmer() {
        isShimmeringStart = false
    }
}