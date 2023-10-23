package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardExpandableItemBinding
import com.design2.chili2.extensions.setTextOrHide

class ExpandableCardItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableCardItemViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_ExpandableCardItemView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    override val rootContainer: View
        get() = vb.rootView

    override val styleableAttrRes: IntArray = R.styleable.ExpandableCardItemView

    private lateinit var vb: ChiliViewCardExpandableItemBinding

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardExpandableItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        getString(R.styleable.ExpandableCardItemView_title).run { setTitle(this) }
        getString(R.styleable.ExpandableCardItemView_subtitle).run { setSubtitle(this) }
        getString(R.styleable.ExpandableCardItemView_titleValue).run { setTitleValue(this) }
        getString(R.styleable.ExpandableCardItemView_subtitleValue).run { setSubtitleValue(this) }
    }

    override fun setupView() {
        super.setupView()
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
    }


    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(vb.tvSubtitle)
        else shimmeringPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitle(resId: Int?) {
        vb.tvSubtitle.setTextOrHide(resId)
        if (resId == null) shimmeringPairs.remove(vb.tvSubtitle)
        else shimmeringPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setTitleValue(charSequence: CharSequence?) {
        vb.tvTitleValue.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(vb.tvTitleValue)
        else shimmeringPairs[vb.tvTitleValue] = vb.viewTitleValueShimmer
    }

    fun setTitleValue(resId: Int) {
        vb.tvTitleValue.setText(resId)
        shimmeringPairs[vb.tvTitleValue] = vb.viewTitleValueShimmer
    }

    fun setSubtitleValue(charSequence: CharSequence?) {
        vb.tvSubtitleValue.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(vb.tvSubtitleValue)
        else shimmeringPairs[vb.tvSubtitleValue] = vb.viewSubtitleValueShimmer
    }

    fun setSubtitleValue(resId: Int) {
        vb.tvSubtitleValue.setText(resId)
        shimmeringPairs[vb.tvSubtitleValue] = vb.viewSubtitleValueShimmer
    }
}