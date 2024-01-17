package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewDiscountCardBinding
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.recolorDrawable
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible

class IconHolderCardItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.categoryCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CategoryCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewDiscountCardBinding

    override val styleableAttrRes: IntArray = R.styleable.CategoryCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewDiscountCardBinding.inflate(LayoutInflater.from(context), this, true)
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
        shimmeringPairs[vb.ivIcon] = vb.viewIconShimmer
        shimmeringPairs[vb.tvLabel] = vb.viewLabelShimmer
    }

    fun setEmoji(emoji: String) = with(vb) {
        tvEmoji.visible()
        ivEmojiHolder.invisible()
        ivIcon.visible()
        tvEmoji.text = emoji
    }

    fun setColor(@ColorRes colorRes: Int?) {
        colorRes?.let {
            vb.placeHolder.background = vb.placeHolder.background.mutate().recolorDrawable(context.color(colorRes))
        }
    }

    fun setColor(color: String?) {
        color?.let {
            vb.placeHolder.background = vb.placeHolder.background.mutate()
                    .recolorDrawable(Color.parseColor(it))
        }
    }

    fun setIsEllipsizeEnd(isEnabled: Boolean) {
        vb.tvLabel.ellipsize = if (isEnabled) TextUtils.TruncateAt.END else null
    }

    fun setPlaceHolder(@DrawableRes resId: Int) {
        vb.placeHolder.setBackgroundResource(resId)
        vb.ivEmojiHolder.invisible()
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(@DrawableRes resId: Int) {
        vb.tvEmoji.invisible()
        vb.ivEmojiHolder.invisible()
        vb.ivIcon.setImageResource(resId)
    }

    fun setEmojiPlaceholderIcon(@DrawableRes resId: Int) {
        vb.tvEmoji.invisible()
        vb.ivIcon.visible()
        vb.ivEmojiHolder.visible()
        vb.ivEmojiHolder.setImageResource(resId)
    }

    fun setIcon(url: String) = with(vb) {
        tvEmoji.invisible()
        ivEmojiHolder.invisible()
        tvEmoji.invisible()
        ivIcon.visible()
        ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.tvEmoji.invisible()
        vb.ivIcon.setImageDrawable(drawable)
    }
}