package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout

class BalanceCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.balanceCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_BalanceCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: BalanceCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.BalanceCardView

    override val rootContainer: View
        get() = view.root

    override fun inflateView(context: Context) {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chili_view_card_balance, this, true)
        this.view = BalanceCardViewViewVariables(
            tvLabel = view.findViewById(R.id.tv_title),
            tvValue = view.findViewById(R.id.tv_value),
            ivIcon = view.findViewById(R.id.iv_icon),
            ivEndIcon = view.findViewById(R.id.iv_end_icon),
            ivTitleIcon = view.findViewById(R.id.iv_title_icon),
            ivChevron = view.findViewById(R.id.iv_chevron),
            root = view.findViewById(R.id.root_view),
            titleShimmering = view.findViewById(R.id.view_title_shimmer),
            subtitleShimmering = view.findViewById(R.id.view_subtitle_shimmer)
        )
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
        getDrawable(R.styleable.BalanceCardView_titleIcon).let {
            setTitleIcon(it)
        }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[view.tvLabel] = view.titleShimmering
        shimmeringPairs[view.tvValue] = view.subtitleShimmering
        shimmeringPairs[view.ivChevron] = null
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

    fun setValueText(charSequence: CharSequence?) {
        view.tvValue.text = charSequence
    }

    fun setValueText(resId: Int) {
        view.tvValue.setText(resId)
    }

    fun setValueTextTextAppearance(resId: Int) {
        view.tvValue.setTextAppearance(resId)
    }

    fun setValueTextColor(@ColorInt colorId: Int) {
        view.tvValue.setTextColor(colorId)
    }

    fun setIcon(resId: Int?) = with (view.ivIcon) {
        if (resId == null) {
            gone()
            shimmeringPairs.remove(this)
        } else {
            setImageResource(resId)
            visible()
            shimmeringPairs[this] = null
        }
    }

    fun setIcon(drawable: Drawable?) = with(view.ivIcon) {
        if (drawable == null) gone()
        else {

            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = null

        }
    }

    fun setTitleIcon(drawable: Drawable?) = with(view.ivTitleIcon) {
        if (drawable == null) gone()
        else {
            visible()
            setImageDrawable(drawable)
        }
    }

    fun setTitleIcon(drawableRes: Int?) = with(view.ivTitleIcon) {
        if (drawableRes == null) gone()
        else {
            visible()
            setImageResource(drawableRes)
        }
    }

    fun setEndIcon(drawable: Drawable?) = with(view.ivEndIcon) {
        if (drawable == null) gone()
        else {
            visible()
            setImageDrawable(drawable)
        }
    }

    fun setEndIcon(drawableRes: Int?) = with(view.ivEndIcon) {
        if (drawableRes == null) gone()
        else {
            visible()
            setImageResource(drawableRes)
        }
    }

    fun isChevronVisible(isVisible: Boolean) {
        view.ivChevron.isVisible = isVisible
    }
}

data class BalanceCardViewViewVariables(
    val tvLabel: TextView,
    val tvValue: TextView,
    val ivIcon: ImageView,
    val ivEndIcon: ImageView,
    val ivTitleIcon: ImageView,
    val ivChevron: ImageView,
    val root: ConstraintLayout,
    val titleShimmering: ShimmerFrameLayout,
    val subtitleShimmering: ShimmerFrameLayout,
)