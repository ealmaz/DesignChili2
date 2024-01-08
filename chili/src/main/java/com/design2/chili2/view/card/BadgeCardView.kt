package com.design2.chili2.view.card

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBadgeBinding
import com.design2.chili2.extensions.color

class BadgeCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : CardView(context, attrs) {

    private lateinit var vb: ChiliViewBadgeBinding

    init {
        initView(context)
        obtainAttributes(context, attrs)
    }

    private fun initView(context: Context) {
        vb = ChiliViewBadgeBinding.inflate(LayoutInflater.from(context), this, true)
    }


    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.BadgeCardView).run {
            getString(R.styleable.BadgeCardView_title)?.let {
                setTitle(it)
            }
            getResourceId(R.styleable.BadgeCardView_cardBackground, -1)
                .takeIf { it != -1 }?.let { setCardBackground(it) }
            getResourceId(R.styleable.BadgeCardView_titleTextColor, R.color.white_1)
                .takeIf { it != -1 }?.let { vb.tvText.setTextColor(context.color(it)) }
            setIcon(getDrawable(R.styleable.BadgeCardView_icon))
            recycle()
        }
    }

    fun setCardBackground(resId: Int) {
        vb.contentContainer.setBackgroundResource(resId)
    }

    fun setCardBackground(drawable: Drawable) {
        vb.contentContainer.background = drawable
    }

    fun setIcon(@DrawableRes drawableResId: Int) {
        vb.ivIcon.setImageResource(drawableResId)
    }

    fun setIcon(drawable: Drawable?) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setTitle(text: String) {
        vb.tvText.text = text
    }

    fun setTitle(@StringRes textResId: Int) {
        vb.tvText.setText(textResId)
    }

    fun setTitle(spanned: Spanned) {
        vb.tvText.text = spanned
    }
}