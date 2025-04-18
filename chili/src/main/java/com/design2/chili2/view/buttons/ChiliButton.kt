package com.design2.chili2.view.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewButtonBinding
import com.design2.chili2.extensions.getPixelSizeFromAttr
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.visible

class ChiliButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chiliButtonDefaultStyle,
    defStyle: Int = R.style.Chili_ButtonStyle
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle) {

    private lateinit var vb: ChiliViewButtonBinding

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
    }

    private fun initView(context: Context) {
        vb = ChiliViewButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.ChiliButton, defStyleAttr, defStyle).run {
            setText(getString(R.styleable.ChiliButton_android_text))
            setEnabled(getBoolean(R.styleable.ChiliButton_android_enabled, true))
            getResourceId(R.styleable.ChiliButton_android_textAppearance, -1).takeIf { it != -1 }?.let {
                setTextAppearance(it)
            }
            setupStartIconSize(
                widthPx = getDimensionPixelSize(R.styleable.IconedButton_startIconWidth, -1).takeIf { it != -1 },
                heightPx = getDimensionPixelSize(R.styleable.IconedButton_startIconHeight, -1).takeIf { it != -1 }
            )
            setStartIcon(
                getResourceId(R.styleable.IconedButton_startIcon, -1).takeIf { it != -1 }
            )
            setIsLoading(getBoolean(R.styleable.ChiliButton_isLoading, false))
            setLoaderColor(
                getResourceId(R.styleable.ChiliButton_loaderColor, -1).takeIf { it != -1 }
            )
            recycle()
        }
    }

    fun setText(text: String?) {
        vb.tvTitle.text = text
    }

    fun setText(@StringRes textResId: Int) {
        vb.tvTitle.setText(textResId)
    }

    fun setIsLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> showLoader()
            else -> hideLoader()
        }
    }

    fun setTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setStartIcon(@DrawableRes drawableRes: Int?) {
        vb.ivStartIcon.setImageOrHide(drawableRes)
    }

    fun setStartIcon(url: String?) {
        vb.ivStartIcon.setImageOrHide(url)
    }

    fun setStartIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupStartIconSize(widthPx, heightPx)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vb.tvTitle.isEnabled = enabled
    }

    private fun setupStartIconSize(widthPx: Int?, heightPx: Int?) {
        vb.ivStartIcon.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
        }
    }

    private fun showLoader() = with(vb) {
        val paddingTop = context.getPixelSizeFromAttr(R.attr.ChiliLoadingButtonPaddingTop)
        val paddingBottom = context.getPixelSizeFromAttr(R.attr.ChiliLoadingButtonPaddingBottom)

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        isClickable = false
        isFocusable = false
        llRoot.invisible()
        progress.visible()
    }

    private fun hideLoader() = with (vb) {
        val paddingTop = context.getPixelSizeFromAttr(R.attr.ChiliPrimaryButtonPaddingTop)
        val paddingBottom = context.getPixelSizeFromAttr(R.attr.ChiliPrimaryButtonPaddingBottom)

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

        llRoot.visible()
        progress.gone()
        isClickable = true
        isFocusable = true
    }

    fun setLoaderColor(@DrawableRes colorResId: Int?) {
        vb.progress.indeterminateTintList = context.getColorStateList(colorResId ?: R.color.magenta_1)
    }
}