package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardSimplePromoBannerBinding
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.loadImage
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible

class SimplePromoBannerCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.simplePromoBannerCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_SimplePromoBannerCard
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardSimplePromoBannerBinding

    override val styleableAttrRes: IntArray = R.styleable.SimplePromoBannerCardView

    override val rootContainer: View
        get() = vb.rootView

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardSimplePromoBannerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.SimplePromoBannerCardView_title))
        setSubtitle(getText(R.styleable.SimplePromoBannerCardView_subtitle))

        getResourceId(R.styleable.SimplePromoBannerCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }

        getResourceId(R.styleable.SimplePromoBannerCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }

        getResourceId(R.styleable.SimplePromoBannerCardView_titleTextColor, R.color.white_1)
            .takeIf { it != -1 }?.let { vb.tvTitle.setTextColor(context.color(it)) }

        getResourceId(R.styleable.SimplePromoBannerCardView_subtitleTextColor, R.color.white_1)
            .takeIf { it != -1 }?.let { vb.tvSubtitle.setTextColor(context.color(it)) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
    }

    override fun onStartShimmer() {
        super.onStartShimmer()
        vb.ivCardBackground.gone()
    }

    override fun onStopShimmer() {
        super.onStopShimmer()
        vb.ivCardBackground.visible()
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

    fun setCardBackgroundByUrl(url: String?) {
        url?.let { vb.ivCardBackground.loadImage(it) }
    }
}