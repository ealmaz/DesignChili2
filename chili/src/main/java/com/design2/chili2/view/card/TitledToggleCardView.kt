package com.design2.chili2.view.card

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.core.text.parseAsHtml
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewTitledToggleCardBinding

class TitledToggleCardView : FrameLayout {

    private lateinit var vb: ChiliViewTitledToggleCardBinding

    constructor(context: Context) : super(context) {
        inflateViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateViews()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        inflateViews()
        obtainAttributes(attrs, defStyle)
    }

    @SuppressLint("MissingInflatedId")
    private fun inflateViews() {
        vb = ChiliViewTitledToggleCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_CardViewStyle_SingleSelectedCard) {
        context?.obtainStyledAttributes(attrs, R.styleable.SingleSelectedCardView, R.attr.singleSelectedCardViewDefaultStyle, defStyle)?.run {
            getString(R.styleable.SingleSelectedCardView_title)?.let {
                setTitleText(it)
            }
            getString(R.styleable.SingleSelectedCardView_value)?.let {
                setValue(it)
            }
            recycle()
        }
    }

    fun setTitleText(text: String) {
        vb.mitcvView.setTitle(text)
    }

    fun setValue(value: String) {
        vb.tcvToggle.setTitle(value)
    }

    fun setValueHtml(value: String) {
        vb.tcvToggle.setTitle(value.parseAsHtml())
    }

    fun setIcons(icons: ArrayList<String>) {
        vb.mitcvView.setIcons(icons)
    }

    fun setIsInfoButtonVisible(isVisible: Boolean) {
        vb.mitcvView.setIsInfoButtonVisible(isVisible)
    }

    fun setInfoButtonClickListener(onClick: () -> Unit) {
        vb.mitcvView.setInfoButtonClickListener(onClick)
    }

    fun setOnCheckChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        vb.tcvToggle.setOnCheckChangeListener(listener)
    }

    fun setUnavailable(isUnavailable: Boolean) {
        vb.tcvToggle.setIsEnabled(!isUnavailable)
    }
}