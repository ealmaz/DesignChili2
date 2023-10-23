package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardPaymentBinding
import com.design2.chili2.extensions.setImageByUrl

class PaymentCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.paymentCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PaymentCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardPaymentBinding

    override val styleableAttrRes: IntArray = R.styleable.PaymentCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewCardPaymentBinding.inflate(LayoutInflater.from(context), this, true)
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
        vb.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setGravity(gravity: Int) {
        vb.root.gravity = gravity
    }
}