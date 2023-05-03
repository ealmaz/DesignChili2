package com.design2.chili2.view.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.design2.chili2.R
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible

class TitledDividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.titledDividerDefaultStyle,
    defStyleRes: Int = R.style.Chili_TitledDividerViewStyle
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: TitledDiverViewVariables

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_titled_divider, this, true)
        this.view = TitledDiverViewVariables(
            tvTitle = view.findViewById(R.id.tv_title),
            ivIcon = view.findViewById(R.id.iv_icon),
            tvActionText = view.findViewById(R.id.tv_action)
        )
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
        view.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        view.tvTitle.setTextAppearance(resId)
    }

    fun setActionText(charSequence: CharSequence?) {
        view.tvActionText.setTextOrHide(charSequence)
    }

    fun setActionText(resId: Int) {
        view.tvActionText.setTextOrHide(resId)
    }

    fun setActionTextTextAppearance(resId: Int) {
        view.tvActionText.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        view.ivIcon.apply {
            visible()
            setImageResource(resId)
        }
    }

    fun setIcon(drawable: Drawable) {
        view.ivIcon.apply {
            visible()
            setImageDrawable(drawable)
        }
    }
}

data class TitledDiverViewVariables(
    val tvTitle: TextView,
    val ivIcon: ImageView,
    val tvActionText: TextView,
)