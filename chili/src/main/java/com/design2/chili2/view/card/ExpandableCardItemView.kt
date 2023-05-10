package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.R
import com.design2.chili2.extensions.setTextOrHide
import com.facebook.shimmer.ShimmerFrameLayout

class ExpandableCardItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableCardItemViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_ExpandableCardItemView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    override val rootContainer: View
        get() = view.root

    override val styleableAttrRes: IntArray = R.styleable.ExpandableCardItemView

    private lateinit var view: ExpandableCardItemViewVariables

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_card_expandable_item, this, true)
        this.view = ExpandableCardItemViewVariables(
            root = view.findViewById(R.id.root_view),
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            tvTitleValue = view.findViewById(R.id.tv_title_value),
            tvSubtitleValue = view.findViewById(R.id.tv_subtitle_value),
            tvTitleShimmer = view.findViewById(R.id.view_title_shimmer),
            tvSubtitleShimmer = view.findViewById(R.id.view_subtitle_shimmer),
            tvTitleValueShimmer = view.findViewById(R.id.view_title_value_shimmer),
            tvSubtitleValueShimmer = view.findViewById(R.id.view_subtitle_value_shimmer),
        )
    }

    override fun TypedArray.obtainAttributes() {
        getString(R.styleable.ExpandableCardItemView_title).run { setTitle(this) }
        getString(R.styleable.ExpandableCardItemView_subtitle).run { setSubtitle(this) }
        getString(R.styleable.ExpandableCardItemView_titleValue).run { setTitleValue(this) }
        getString(R.styleable.ExpandableCardItemView_subtitleValue).run { setSubtitleValue(this) }
    }

    override fun setupView() {
        shimmeringPairs[view.tvTitle] = view.tvTitleShimmer
    }


    fun setTitle(charSequence: CharSequence?) {
        view.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        view.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(view.tvSubtitle)
        else shimmeringPairs[view.tvSubtitle] = view.tvSubtitleShimmer
    }

    fun setSubtitle(resId: Int?) {
        view.tvSubtitle.setTextOrHide(resId)
        if (resId == null) shimmeringPairs.remove(view.tvSubtitle)
        else shimmeringPairs[view.tvSubtitle] = view.tvSubtitleShimmer
    }

    fun setTitleValue(charSequence: CharSequence?) {
        view.tvTitleValue.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(view.tvTitleValue)
        else shimmeringPairs[view.tvTitleValue] = view.tvTitleValueShimmer
    }

    fun setTitleValue(resId: Int) {
        view.tvTitleValue.setText(resId)
        shimmeringPairs[view.tvTitleValue] = view.tvTitleValueShimmer
    }

    fun setSubtitleValue(charSequence: CharSequence?) {
        view.tvSubtitleValue.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(view.tvSubtitleValue)
        else shimmeringPairs[view.tvSubtitleValue] = view.tvSubtitleValueShimmer
    }

    fun setSubtitleValue(resId: Int) {
        view.tvSubtitleValue.setText(resId)
        shimmeringPairs[view.tvSubtitleValue] = view.tvSubtitleValueShimmer
    }
}

data class ExpandableCardItemViewVariables(
    val root: ConstraintLayout,
    val tvTitle: TextView,
    val tvSubtitle: TextView,
    val tvTitleValue: TextView,
    val tvSubtitleValue: TextView,
    val tvTitleShimmer: ShimmerFrameLayout,
    val tvSubtitleShimmer: ShimmerFrameLayout,
    val tvTitleValueShimmer: ShimmerFrameLayout,
    val tvSubtitleValueShimmer: ShimmerFrameLayout,
)