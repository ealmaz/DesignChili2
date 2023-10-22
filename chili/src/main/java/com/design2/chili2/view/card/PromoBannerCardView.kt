package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardBannerBinding
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.Shimmer

class PromoBannerCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.promoBannerCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PromoBannerCard
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardBannerBinding

    override val styleableAttrRes: IntArray = R.styleable.PromoBannerCardView

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardBannerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        getResourceId(R.styleable.PromoBannerCardView_android_background, -1)
            .takeIf { it != -1 }?.let { setBackgroundResId(it) }
        getString(R.styleable.PromoBannerCardView_title)?.let { setTitleText(it) }
        getString(R.styleable.PromoBannerCardView_subtitle)?.let { setSubtitleText(it) }
        setChevronVisibility(!getBoolean(R.styleable.PromoBannerCardView_hide_chevron, true))
        getResourceId(R.styleable.PromoBannerCardView_android_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        getResourceId(R.styleable.PromoBannerCardView_right_image, -1)
            .takeIf { it != -1 }?.let { setRightImage(it) }
        getResourceId(R.styleable.PromoBannerCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextTextAppearance(it) }
        getResourceId(R.styleable.PromoBannerCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextTextAppearance(it) }
        getResourceId(R.styleable.PromoBannerCardView_titleTextColor, R.color.white_1)
            .takeIf { it != -1 }?.let { vb.tvTitle.setTextColor(context.color(it)) }
        getResourceId(R.styleable.PromoBannerCardView_subtitleTextColor, R.color.white_1)
            .takeIf { it != -1 }?.let { vb.tvSubTitle.setTextColor(context.color(it)) }
        radius = getDimension(R.styleable.PromoBannerCardView_cardCornerRadius, 12f)
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.shimmerSubtitle] = vb.shimmerSubtitle
        onStopShimmer()
    }

    override fun onStartShimmer() {
        val subtitleShimmer = Shimmer.ColorHighlightBuilder()
            .setHighlightAlpha(1f)
            .setBaseAlpha(1f)
            .setWidthRatio(2.0F)
            .setBaseColor(context.color(R.color.magenta_1))
            .setHighlightColor(context.color(R.color.gray_8))
            .build()
        vb.shimmerSubtitle.setShimmer(subtitleShimmer)
    }

    override fun onStopShimmer() {
        vb.shimmerSubtitle.setShimmer(null)
    }

    private fun setIcon(iconResId: Int) {
        vb.ivIcon.apply {
            setImageDrawable(context.drawable(iconResId))
            visible()
        }
    }

    fun setTitleText(text: String) {
        vb.tvTitle.text = text
    }

    fun setSubtitleText(text: String) {
        vb.tvSubTitle.text = text
    }

    fun setSubtitleColor(@ColorRes colorResId: Int) {
        vb.tvSubTitle.setTextColor(context.color(colorResId))
    }

    fun setChevronVisibility(isVisible: Boolean) {
        vb.ivChevron.isVisible = isVisible
    }

    private fun setRightImage(imgResId: Int) {
        vb.ivRightImage.apply {
            setImageDrawable(context.drawable(imgResId))
            visible()
        }
    }

    fun setBackgroundResId(@DrawableRes resId: Int) {
        vb.root.setBackgroundResource(resId)
    }

    private fun setTitleTextTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    private fun setSubtitleTextTextAppearance(@StyleRes resId: Int) {
        vb.tvSubTitle.setTextAppearance(resId)
    }

    fun setSubtitleContainerBackground(@DrawableRes resId: Int) {
        vb.llSubtitleContainer.setBackgroundResource(resId)
        if (resId == 0) {
            vb.llSubtitleContainer.setPadding(0, 0, 0, 0)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        vb.root.setOnClickListener(l)
    }
}