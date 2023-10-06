package com.design2.chili2.view.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class PromoBannerCardView : CardView {
    private lateinit var view: PromoBannerCardViewVariables

    constructor(context: Context) : super(context) {
        inflateViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateViews()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        inflateViews()
        obtainAttributes(attrs)
    }


    private fun inflateViews() {
        val view = LayoutInflater.from(context).inflate(R.layout.chilli_view_banner_card, this)
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
        cardElevation = 0.0f
        radius = resources.getDimension(R.dimen.radius_12dp)
    }

    private fun obtainAttributes(attrs: AttributeSet) {
        context?.obtainStyledAttributes(attrs, R.styleable.PromoBannerCardView)?.run {
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
            setSubtitleShimmer(null)
            recycle()
        }
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

    fun setSubtitleShimmerVisibility(isVisible: Boolean) {
        if (isVisible) {
            view.subtitleShimmer.startShimmer()
        } else {
            view.subtitleShimmer.stopShimmer()
        }
    }

    fun setSubtitleShimmer(shimmer: Shimmer?) {
        view.subtitleShimmer.setShimmer(shimmer)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        view.root.setOnClickListener(l)
    }

}

private data class PromoBannerCardViewVariables(
    var root: ConstraintLayout,
    var title: TextView,
    var subTitle: TextView,
    var rightImage: ImageView,
    var icon: ImageView,
    var chevron: ImageView,
    var subtitleShimmer: ShimmerFrameLayout,
    var subtitleContainer: LinearLayout
)