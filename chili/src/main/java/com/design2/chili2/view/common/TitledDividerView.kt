package com.design2.chili2.view.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewTitledDividerBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible

class TitledDividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.titledDividerDefaultStyle,
    defStyleRes: Int = R.style.Chili_TitledDividerViewStyle
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewTitledDividerBinding

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewTitledDividerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.TitledDividerView, defStyleAttr, defStyleRes).run {
            setTitle(getText(R.styleable.TitledDividerView_title))
            setActionText(getText(R.styleable.TitledDividerView_actionText))
            getResourceId(R.styleable.TitledDividerView_titleTextAppearance, -1)
                .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
            getResourceId(R.styleable.TitledDividerView_actionTextTextAppearance, -1)
                .takeIf { it != -1 }?.let { setActionTextTextAppearance(it) }
            getResourceId(R.styleable.TitledDividerView_startIcon, -1)
                .takeIf { it != -1 }?.let { setIcon(it) }
            recycle()
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setActionText(charSequence: CharSequence?) {
        vb.tvAction.setTextOrHide(charSequence)
    }

    fun setActionText(resId: Int) {
        vb.tvAction.setTextOrHide(resId)
    }

    fun setActionTextTextAppearance(resId: Int) {
        vb.tvAction.setTextAppearance(resId)
    }

    fun setOnActionTextClickListener(onClick: () -> Unit) {
        vb.tvAction.setOnSingleClickListener(onClick)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.apply {
            visible()
            setImageResource(resId)
        }
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.apply {
            visible()
            setImageDrawable(drawable)
        }
    }
}