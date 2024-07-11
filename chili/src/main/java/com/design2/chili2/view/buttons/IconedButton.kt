package com.design2.chili2.view.buttons

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewButtonIconedBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible

class IconedButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.loaderButtonDefaultStyle,
    defStyle: Int = R.style.Chili_ButtonStyle
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle) {

    private lateinit var vb: ChiliViewButtonIconedBinding

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
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
            getResourceId(R.styleable.IconedButton_startIcon, -1).takeIf { it != -1 }
                ?.let(::setStartIcon)
            setupStartIconSize(
                width = getDimensionPixelSize(R.styleable.IconedButton_startIconWidth, -1).takeIf { it != -1 },
                height = getDimensionPixelSize(R.styleable.IconedButton_startIconHeight, -1).takeIf { it != -1 }
            )
            setStartIconVisibility(getBoolean(R.styleable.IconedButton_isStartIconVisible, false))
            setText(getString(R.styleable.IconedButton_android_text))
            setEnabled(getBoolean(R.styleable.IconedButton_android_enabled, true))
            getResourceId(R.styleable.IconedButton_android_textAppearance, -1).takeIf { it != -1 }
                ?.let { setTextAppearance(it) }
            recycle()
        }
    }

    fun setStartIcon(string: String) {
        vb.ivStartIcon.apply {
            visible()
            setImageByUrl(string)
        }
    }

    fun setStartIcon(@DrawableRes drawableRes: Int) {
        vb.ivStartIcon.apply {
            visible()
            setImageResource(drawableRes)
        }
    }

    fun setupStartIconSize(width: Int?, height: Int?) {
        vb.ivStartIcon.apply {
            width?.let { layoutParams.width = it }
            height?.let { layoutParams.height = it }
        }
    }

    fun setStartIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupStartIconSize(widthPx, heightPx)
    }

    private fun setupStartIconSize(widthPx: Int, heightPx: Int) {
        val params = vb.ivStartIcon.layoutParams
        params.height = heightPx
        params.width = widthPx
        vb.ivStartIcon.layoutParams = params
    }

    fun setStartIconVisibility(isIconVisible: Boolean) {
        vb.ivStartIcon.run {
            if (isIconVisible) visible() else gone()
        }
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

    fun setIcon(@DrawableRes drawableRes: Int) {
        vb.ivIcon.apply {
            visible()
            setImageResource(drawableRes)
        }
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
}