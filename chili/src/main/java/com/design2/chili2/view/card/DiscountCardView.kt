package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.recolorDrawable
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.card.BaseCardView
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.shimmer.ShimmerFrameLayout

class DiscountCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.categoryCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CategoryCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: DiscountCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.CategoryCardView

    override val rootContainer: View
        get() = view.root

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_discount_card, this, true)
        this.view = DiscountCardViewViewVariables(
            tvLabel = view.findViewById(R.id.tv_label),
            ivIcon = view.findViewById(R.id.iv_icon),
            ivEmojiHolder = view.findViewById(R.id.iv_emoji_holder),
            root = view.findViewById(R.id.root_view),
            tvEmoji = view.findViewById(R.id.tv_emoji),
            placeholder = view.findViewById(R.id.superellipse_icon),
            labelShimmer = view.findViewById(R.id.view_label_shimmer),
            iconShimmer = view.findViewById(R.id.view_icon_shimmer),
        )
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.CategoryCardView_title))
        getResourceId(R.styleable.CategoryCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.CategoryCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[view.ivIcon] = view.iconShimmer
        shimmeringPairs[view.tvLabel] = view.labelShimmer
    }

    fun setEmoji(emoji: String) {
        view.tvEmoji.visible()
        view.ivEmojiHolder.visible()
        view.ivIcon.invisible()
        view.tvEmoji.text = emoji
    }

    fun setColor(@ColorRes colorRes: Int?) {
        colorRes?.let {
            view.placeholder.background = view.placeholder.background.mutate().recolorDrawable(context.color(colorRes))
        }
    }

    fun setColor(color: String?) {
        color?.let {
            view.placeholder.background = view.placeholder.background.mutate()
                    .recolorDrawable(Color.parseColor(it))
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        view.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        view.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(@DrawableRes resId: Int) {
        view.tvEmoji.invisible()
        view.ivEmojiHolder.invisible()
        view.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        view.tvEmoji.invisible()
        view.ivEmojiHolder.visible()
        view.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        view.tvEmoji.invisible()
        view.ivIcon.setImageDrawable(drawable)
    }
}

data class DiscountCardViewViewVariables(
    val tvLabel: TextView,
    val ivIcon : ImageView,
    val ivEmojiHolder: SimpleDraweeView,
    val placeholder: ConstraintLayout,
    val tvEmoji: TextView,
    val root: ConstraintLayout,
    val labelShimmer: ShimmerFrameLayout,
    val iconShimmer: ShimmerFrameLayout,

)