package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardAccentBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible

class AccentCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.accentCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_AccentCardViewStyle
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardAccentBinding

    override val styleableAttrRes: IntArray = R.styleable.AccentCardView

    override val rootContainer: View
        get() = vb.rootView

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardAccentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.AccentCardView_title))
        setSubtitle(getText(R.styleable.AccentCardView_subtitle))

        getResourceId(R.styleable.AccentCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }

        getResourceId(R.styleable.AccentCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }

        getResourceId(R.styleable.AccentCardView_titleStartIcon, -1)
            .takeIf { it != -1 }?.let { setTitleStartIcon(it) }

        getResourceId(R.styleable.AccentCardView_endIcon, -1)
            .takeIf { it != -1 }?.let { setEndIcon(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
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


    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(vb.tvSubtitle)
        else shimmeringPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitle(resId: Int) {
        vb.tvSubtitle.setTextOrHide(resId)
        shimmeringPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitleTextAppearance(resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setTitleStartIcon(resId: Int?) = with(vb.ivStartIcon) {
        if (resId == null) {
            gone()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageResource(resId)
            shimmeringPairs[this] = vb.viewStartIconShimmer
        }
    }

    fun setTitleStartIcon(drawable: Drawable?) = with(vb.ivStartIcon) {
        if (drawable == null) {
            gone()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = vb.viewStartIconShimmer
        }
    }

    fun setEndIcon(resId: Int?) = with(vb.ivEndIcon) {
        if (resId == null) {
            invisible()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageResource(resId)
            shimmeringPairs[this] = vb.viewEndIconShimmer
        }
    }

    fun setEndIcon(drawable: Drawable?) = with(vb.ivEndIcon) {
        if (drawable == null) {
            invisible()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = vb.viewEndIconShimmer
        }
    }
}