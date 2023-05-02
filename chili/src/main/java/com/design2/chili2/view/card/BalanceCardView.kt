package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.design2.chili2.R
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible

class BalanceCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.balanceCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_BalanceCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: BalanceCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.BalanceCardView

    override val rootContainer: View
        get() = view.root

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_balance_card, this, true)
        this.view = BalanceCardViewViewVariables(
            tvLabel = view.findViewById(R.id.tv_title),
            tvValue = view.findViewById(R.id.tv_value),
            ivIcon = view.findViewById(R.id.iv_icon),
            root = view.findViewById(R.id.root_view)
        )
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.BalanceCardView_title))
        setValueText(getText(R.styleable.BalanceCardView_value))
        getResourceId(R.styleable.BalanceCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.BalanceCardView_valueTextAppearance, -1)
            .takeIf { it != -1 }?.let { setValueTextTextAppearance(it) }
        getResourceId(R.styleable.BalanceCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
    }

    fun setTitle(charSequence: CharSequence) {
        view.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        view.tvLabel.setTextAppearance(resId)
    }

    fun setValueText(charSequence: CharSequence?) {
        view.tvValue.setTextOrHide(charSequence)
    }

    fun setValueText(resId: Int) {
        view.tvValue.setTextOrHide(resId)
    }

    fun setValueTextTextAppearance(resId: Int) {
        view.tvValue.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        view.ivIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    fun setIcon(drawable: Drawable) {
        view.ivIcon.apply {
            visible()
            setImageDrawable(drawable)
        }
    }
}

data class BalanceCardViewViewVariables(
    val tvLabel: TextView,
    val tvValue: TextView,
    val ivIcon: ImageView,
    val root: LinearLayout
)