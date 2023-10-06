package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class PromoBannerCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.promoBannerCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PromoBannerCard
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: PromoBannerCardViewVariables

    override val styleableAttrRes: IntArray = R.styleable.PromoBannerCardView

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_card_banner, this)
        this.view = PromoBannerCardViewVariables(
            root = view.findViewById(R.id.cl_container),
            title = view.findViewById(R.id.tv_title),
            subTitle = view.findViewById(R.id.tv_sub_title),
            rightImage = view.findViewById(R.id.iv_right_image),
            icon = view.findViewById(R.id.iv_icon),
            chevron = view.findViewById(R.id.iv_chevron),
            subtitleShimmer = view.findViewById(R.id.shimmer_subtitle),
            subtitleContainer = view.findViewById(R.id.ll_subtitle_container),
        )
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
            .takeIf { it != -1 }?.let { view.title.setTextColor(context.color(it)) }
        getResourceId(R.styleable.PromoBannerCardView_subtitleTextColor, R.color.white_1)
            .takeIf { it != -1 }?.let { view.subTitle.setTextColor(context.color(it)) }
        radius = getDimension(R.styleable.PromoBannerCardView_cardCornerRadius, 12f)
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[view.subtitleShimmer] = view.subtitleShimmer
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
        view.subtitleShimmer.setShimmer(subtitleShimmer)
    }

    override fun onStopShimmer() {
        view.subtitleShimmer.setShimmer(null)
    }

    private fun setIcon(iconResId: Int) {
        view.icon.apply {
            setImageDrawable(context.drawable(iconResId))
            visible()
        }
    }

    fun setTitleText(text: String) {
        view.title.text = text
    }

    fun setSubtitleText(text: String) {
        view.subTitle.text = text
    }

    fun setSubtitleColor(@ColorRes colorResId: Int) {
        view.subTitle.setTextColor(context.color(colorResId))
    }

    fun setChevronVisibility(isVisible: Boolean) {
        view.chevron.isVisible = isVisible
    }

    private fun setRightImage(imgResId: Int) {
        view.rightImage.apply {
            setImageDrawable(context.drawable(imgResId))
            visible()
        }
    }

    fun setBackgroundResId(@DrawableRes resId: Int) {
        view.root.setBackgroundResource(resId)
    }

    private fun setTitleTextTextAppearance(@StyleRes resId: Int) {
        view.title.setTextAppearance(resId)
    }

    private fun setSubtitleTextTextAppearance(@StyleRes resId: Int) {
        view.subTitle.setTextAppearance(resId)
    }

    fun setSubtitleContainerBackground(@DrawableRes resId: Int) {
        view.subtitleContainer.setBackgroundResource(resId)
        if (resId == 0) {
            view.subtitleContainer.setPadding(0, 0, 0, 0)
        }
    }

}

data class PromoBannerCardViewVariables(
    var root: ConstraintLayout,
    var title: TextView,
    var subTitle: TextView,
    var rightImage: ImageView,
    var icon: ImageView,
    var chevron: ImageView,
    var subtitleShimmer: ShimmerFrameLayout,
    var subtitleContainer: LinearLayout
)