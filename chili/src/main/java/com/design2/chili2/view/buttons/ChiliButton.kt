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
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setLeftMargin
import com.design2.chili2.extensions.setRightMargin
import com.design2.chili2.extensions.setTopMargin
import com.design2.chili2.extensions.visible

class ChiliButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chiliButtonDefaultStyle,
    defStyle: Int = R.style.Chili_ButtonStyle_Primary
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle) {

    private lateinit var vb: ChiliViewButtonBinding
    private var bIsLoading = false

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
            setStartIcon(
                getResourceId(R.styleable.ChiliButton_startIcon, -1).takeIf { it != -1 }
            )
            setStartIconSize(
                widthDimenRes = getResourceId(R.styleable.ChiliButton_startIconWidth,  R.dimen.view_24dp),
                heightDimenRes = getResourceId(R.styleable.ChiliButton_startIconHeight,  R.dimen.view_24dp)
            )
            setStartIconEndMargin(
                getResourceId(R.styleable.ChiliButton_startIconEndMargin, R.dimen.view_8dp)
            )
            setEndIcon(
                getResourceId(R.styleable.ChiliButton_endIcon, -1).takeIf { it != -1 }
            )
            setupEndIconSize(
                widthPx = getDimensionPixelSize(R.styleable.ChiliButton_endIconWidth, -1).takeIf { it != -1 },
                heightPx = getDimensionPixelSize(R.styleable.ChiliButton_endIconHeight, -1).takeIf { it != -1 }
            )
            setEndIconStartMargin(
                getResourceId(R.styleable.ChiliButton_endIconStartMargin, R.dimen.view_8dp)
            )
            setIsLoading(getBoolean(R.styleable.ChiliButton_isLoading, false))
            setLoaderColor(
                getResourceId(R.styleable.ChiliButton_loaderColor, -1).takeIf { it != -1 }
            )
            setLoaderSize(
                getResourceId(R.styleable.ChiliButton_loaderSize, R.dimen.view_24dp)
            )
            setVerticalPadding(
                getDimensionPixelSize(R.styleable.ChiliButton_android_paddingTop, -1).takeIf { it != -1 },
                getDimensionPixelSize(R.styleable.ChiliButton_android_paddingBottom, -1).takeIf { it != -1 }
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

    fun setText(text: CharSequence?) {
        vb.tvTitle.text = text
    }

    fun setIsLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> showLoading()
            else -> hideLoading()
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

    fun setStartIconEndMargin(@DimenRes marginRes: Int) {
        val margin = resources.getDimensionPixelSize(marginRes)
        vb.ivStartIcon.setRightMargin(margin)
    }

    fun setEndIcon(@DrawableRes drawableRes: Int?) {
        vb.ivEndIcon.setImageOrHide(drawableRes)
    }

    fun setEndIcon(url: String?) {
        vb.ivEndIcon.setImageOrHide(url)
    }

    fun setEndIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupEndIconSize(widthPx, heightPx)
    }

    fun setEndIconStartMargin(@DimenRes marginRes: Int) {
        val margin = resources.getDimensionPixelSize(marginRes)
        vb.ivEndIcon.setLeftMargin(margin)
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

    private fun setupEndIconSize(widthPx: Int?, heightPx: Int?) {
        vb.ivEndIcon.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if(bIsLoading) {
            mergeDrawableStates(drawableState, intArrayOf(R.attr.isLoading))
        }
        return drawableState
    }

    fun showLoading() = with(vb) {
        bIsLoading = true
        refreshDrawableState()
        isClickable = false
        isFocusable = false
        llRoot.invisible()
        progress.visible()
    }

    fun hideLoading() = with (vb) {
        bIsLoading = false
        refreshDrawableState()
        llRoot.visible()
        progress.gone()
        isClickable = true
        isFocusable = true
    }

    private fun setVerticalPadding(top: Int?, bottom: Int?) {
        setPadding(paddingLeft, 0, paddingRight, 0)
        with(vb.tvTitle) {
            top?.let { setTopMargin(it) }
            bottom?.let { setBottomMargin(it) }
        }
    }

    fun setLoaderColor(@DrawableRes colorResId: Int?) {
        vb.progress.indeterminateTintList = context.getColorStateList(colorResId ?: R.color.magenta_1)
    }

    fun setLoaderSize(@DimenRes sizeRes: Int) {
        val size = resources.getDimensionPixelSize(sizeRes)
        vb.progress.apply {
            layoutParams.width = size
            layoutParams.height = size
        }
    }
}