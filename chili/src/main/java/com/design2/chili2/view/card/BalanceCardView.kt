package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardBalanceBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.visible

class BalanceCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.balanceCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_BalanceCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardBalanceBinding

    override val styleableAttrRes: IntArray = R.styleable.BalanceCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewCardBalanceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.BalanceCardView_title))
        setValueText(getText(R.styleable.BalanceCardView_value))
        getResourceId(R.styleable.BalanceCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.BalanceCardView_valueTextAppearance, -1)
            .takeIf { it != -1 }?.let { setValueTextTextAppearance(it) }
        getResourceId(R.styleable.BalanceCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        getDrawable(R.styleable.BalanceCardView_endIcon).let {
            setEndIcon(it)
        }
        getDrawable(R.styleable.BalanceCardView_badgeIcon).let {
            setBadgeIcon(it)
        }
        getDrawable(R.styleable.BalanceCardView_titleIcon).let {
            setTitleIcon(it)
        }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
        shimmeringPairs[vb.tvValue] = vb.viewSubtitleShimmer
        shimmeringPairs[vb.ivChevron] = null
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setValueText(charSequence: CharSequence?) {
        vb.tvValue.text = charSequence
    }

    fun setValueText(resId: Int) {
        vb.tvValue.setText(resId)
    }

    fun setValueTextTextAppearance(resId: Int) {
        vb.tvValue.setTextAppearance(resId)
    }

    fun setValueTextColor(@ColorInt colorId: Int) {
        vb.tvValue.setTextColor(colorId)
    }

    fun setIcon(resId: Int?) = with(vb.ivIcon) {
        if (resId == null) {
            gone()
            shimmeringPairs.remove(this)
        } else {
            setImageResource(resId)
            visible()
            shimmeringPairs[this] = null
        }
    }

    fun setIcon(drawable: Drawable?) = with(vb.ivIcon) {
        if (drawable == null) gone()
        else {

            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = null

        }
    }

    fun setTitleIcon(drawable: Drawable?) = with(vb.ivTitleIcon) {
        if (drawable == null) gone()
        else {
            visible()
            setImageDrawable(drawable)
        }
    }

    fun setTitleIcon(drawableRes: Int?) = with(vb.ivTitleIcon) {
        if (drawableRes == null) gone()
        else {
            visible()
            setImageResource(drawableRes)
        }
    }

    fun setEndIcon(drawable: Drawable?) = with(vb.ivEndIcon) {
        if (drawable == null) gone()
        else {
            visible()
            setImageDrawable(drawable)
        }
    }

    fun setEndIcon(drawableRes: Int?) = with(vb.ivEndIcon) {
        if (drawableRes == null) gone()
        else {
            visible()
            setImageResource(drawableRes)
        }
    }

    fun setValueIcon(resId: Int?) = with(vb.ivValueIcon) {
        if (resId == null) {
            gone()
            shimmeringPairs.remove(this)
        } else {
            setImageResource(resId)
            visible()
            shimmeringPairs[this] = null
        }
    }

    fun setValueIcon(drawable: Drawable?) = with(vb.ivValueIcon) {
        if (drawable == null) gone()
        else {

            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = null

        }
    }

    fun setIsValueIconVisible(isVisible: Boolean) {
        vb.ivValueIcon.isVisible = isVisible
    }

    fun setIsEndIconVisible(isVisible: Boolean) {
        vb.ivEndIcon.isVisible = isVisible
    }

    fun isChevronVisible(isVisible: Boolean) {
        vb.ivChevron.isVisible = isVisible
    }

    fun setBadgeIcon(drawable: Drawable?) = with(vb.ivIconBadge) {
        if (drawable == null) gone()
        else {
            visible()
            setImageDrawable(drawable)
        }
    }
}