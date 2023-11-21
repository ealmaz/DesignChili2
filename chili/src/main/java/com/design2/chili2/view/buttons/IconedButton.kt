package com.design2.chili2.view.buttons

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewButtonIconedBinding
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

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.IconedButton, defStyleAttr, defStyle).run {
            setText(getString(R.styleable.IconedButton_android_text))
            setEnabled(getBoolean(R.styleable.IconedButton_android_enabled, true))
            recycle()
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

    override fun isEnabled(): Boolean {
        return vb.rootView.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        vb.rootView.isEnabled = enabled
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