package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl

class PaymentCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.paymentCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PaymentCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: PaymentCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.PaymentCardView

    override val rootContainer: View
        get() = view.root

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_card_payment, this, true)
        this.view = PaymentCardViewViewVariables(
            tvLabel = view.findViewById(R.id.tv_label),
            ivIcon = view.findViewById(R.id.iv_icon),
            root = view.findViewById(R.id.root_view)
        )
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.PaymentCardView_title))
        getResourceId(R.styleable.PaymentCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.PaymentCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        getInteger(R.styleable.PaymentCardView_android_gravity, Gravity.CENTER).let { gravity ->
            setGravity(gravity)
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

    fun setIcon(resId: Int) {
        view.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        view.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        view.ivIcon.setImageDrawable(drawable)
    }

    fun setGravity(gravity: Int) {
        view.root.gravity = gravity
    }
}

data class PaymentCardViewViewVariables(
    val tvLabel: TextView,
    val ivIcon: ImageView,
    val root: LinearLayout
)