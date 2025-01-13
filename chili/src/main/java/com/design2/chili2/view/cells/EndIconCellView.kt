package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.design2.chili2.R
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.IconSize

open class EndIconCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.endIconCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_EndIcon
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.EndIconCellView, defStyleAttr, defStyleRes)
            .run {
                getResourceId(R.styleable.EndIconCellView_endIcon, -1).let {
                    if (it > -1) setEndIcon(it)
                }
                getBoolean(R.styleable.EndIconCellView_isEndIconVisible, true).let {
                    setIsEndIconVisible(it)
                }
                getBoolean(R.styleable.EndIconCellView_isEndIconClickable, true).let {
                    setIsEndIconClickable(it)
                }
                getResourceId(R.styleable.EndIconCellView_endIconEndMargin, R.dimen.view_8dp).let {
                    setEndIconEndMargin(it)
                }
                getLayoutDimension(R.styleable.EndIconCellView_endIconSize, IconSize.SMALL.value).let {
                    when (it) {
                        IconSize.SMALL.value -> setEndIconSize(IconSize.SMALL)
                        IconSize.MEDIUM.value -> setEndIconSize(IconSize.MEDIUM)
                        IconSize.LARGE.value -> setEndIconSize(IconSize.LARGE)
                        else -> setupEndIconSize(it,it)
                    }
                }
                recycle()
            }
    }

    override fun onStartShimmer() {
        vb.flEndPlaceHolder.gone()
    }

    override fun onStopShimmer() {
        vb.flEndPlaceHolder.visible()
    }

    fun setEndIcon(endIcon: Any?) {
        when (endIcon) {
            is String -> setEndIcon(endIcon)
            is Int -> setEndIcon(endIcon)
            is Drawable -> setEndIcon(endIcon)
        }
    }

    fun setEndIcon(@DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            vb.ivEndIcon.setImageDrawable(context.drawable(drawableRes))
        }
    }

    fun setEndIcon(url: String?) {
        url?.let {
            vb.ivEndIcon.setImageByUrl(url)
        }
    }

    fun setIsEndIconVisible(isVisible: Boolean) {
        vb.ivEndIcon.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setIsEndIconClickable(clickable: Boolean) {
        vb.ivEndIcon.apply {
            isFocusable = clickable
            isClickable = clickable
        }
    }

    fun setEndIconSize(iconSize: IconSize) {
        val size = when(iconSize) {
            IconSize.LARGE -> R.dimen.view_48dp
            IconSize.MEDIUM -> R.dimen.view_46dp
            IconSize.SMALL -> R.dimen.view_32dp
        }
        setEndIconSize(size, size)
    }

    fun setEndIconEndMargin(@DimenRes endMarginRes: Int) {
        val endMarginPx = resources.getDimensionPixelSize(endMarginRes)
        val param = vb.flEndPlaceHolder.layoutParams as? MarginLayoutParams ?: return
        param.marginEnd = endMarginPx
        vb.flEndPlaceHolder.layoutParams = param
    }
}