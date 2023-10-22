package com.design2.chili2.view.card

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewSingleSelectedCardBinding
import com.design2.chili2.extensions.getColorFromAttr
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.IconStatus

class SingleSelectedCardView : FrameLayout {

    private lateinit var vb: ChiliViewSingleSelectedCardBinding

    var isActive = false
    private var color: String? = ""

    constructor(context: Context) : super(context) {
        inflateViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateViews()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        inflateViews()
        obtainAttributes(attrs, defStyle)
    }

    private fun inflateViews() {
        vb = ChiliViewSingleSelectedCardBinding.inflate(LayoutInflater.from(context))
    }

    private fun obtainAttributes(
        attrs: AttributeSet,
        defStyle: Int = R.style.Chili_CardViewStyle_SingleSelectedCard
    ) {
        context?.obtainStyledAttributes(
            attrs,
            R.styleable.SingleSelectedCardView,
            R.attr.singleSelectedCardViewDefaultStyle,
            defStyle
        )?.run {
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
        vb.tvTitle.text = text
    }

    fun setValue(value: String) {
        vb.tvValue.visible()
        vb.tvValue.text = value
    }

    fun setValue(value: Spanned?) {
        value?.let {
            vb.tvValue.visible()
            vb.tvValue.text = value
        }
    }

    fun setValueHtml(value: String?) {
        value?.let {
            vb.tvValue.visible()
            vb.tvValue.text = value.parseAsHtml()
        }
    }

    fun setupColor(color: String) {
        this.color = color
    }

    private fun setupBorder(color: String) {
        val borderBackground = vb.root.background as? GradientDrawable
        borderBackground?.mutate()
        borderBackground?.setStroke(
            context.resources.getDimension(R.dimen.view_2dp).toInt(),
            Color.parseColor(color)
        )
    }

    private fun setupBackground(color: String) {
        val background = vb.clView.background as? GradientDrawable
        background?.mutate()
        background?.setColor(Color.parseColor(color))
        background?.alpha = 51
    }

    private fun setupBorder(color: Int) {
        val borderBackground = vb.root.background as? GradientDrawable
        borderBackground?.mutate()
        borderBackground?.setStroke(context.resources.getDimension(R.dimen.view_2dp).toInt(), color)
    }

    private fun setupBackground(color: Int) {
        val background = vb.clView.background as? GradientDrawable
        background?.mutate()
        background?.setColor(color)
        background?.alpha = 51
    }

    fun setSelected() {
        color?.let {
            setupBorder(it)
            setupBackground(it)
        }
        setStatus(IconStatus.SELECTED)
    }

    fun setActive() {
        setStatus(IconStatus.ACTIVE)
        color?.let { setupBorder(it) }
        isActive = true
    }

    fun reset() {
        setupBorder(context.getColorFromAttr(R.attr.ChiliCardViewBackground))
        setupBackground(context.getColorFromAttr(R.attr.ChiliCardViewBackground))
        if (isActive) setActive()
        else setStatus(IconStatus.UNSELECTED)
    }

    fun setValueTextRes(@StringRes textResId: Int) {
        vb.tvValue.setText(textResId)
    }

    private fun setIconDrawableRes(@DrawableRes resId: Int) {
        vb.ivActionIcon.visible()
        vb.ivActionIcon.setImageResource(resId)
    }

    private fun setStatus(status: IconStatus) {
        vb.ivActionIcon.visible()
        when (status) {
            IconStatus.SELECTED -> setIconDrawableRes(R.drawable.chili_ic_reset)
            IconStatus.ACTIVE -> setIconDrawableRes(R.drawable.chili_ic_done)
            else -> vb.ivActionIcon.invisible()
        }
        setIsIconClickable(status)
    }

    fun setUnavailable(isUnavailable: Boolean) {
        with(vb) {
            root.isClickable = !isUnavailable
            if (isUnavailable) {
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliSecondaryTextColor))
                tvValue.invisible()
                ivActionIcon.invisible()
                setupBorder(context.getColorFromAttr(R.attr.ChiliCardViewBackground))
                setupBackground(context.getColorFromAttr(R.attr.ChiliCardViewBackground))
            } else {
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliMarkedTextColor))
                tvValue.visible()
            }
        }
    }

    private fun setIsIconClickable(status: IconStatus) {
        when (status) {
            IconStatus.SELECTED -> vb.ivActionIcon.isClickable = true
            else -> vb.ivActionIcon.isClickable = false
        }
    }

    fun setOnClickListener(onClick: () -> Boolean) {
        vb.rootView.setOnClickListener {
            val isCheckedNewValue = onClick.invoke()
            if (isCheckedNewValue) {
                setSelected()
            } else {
                reset()
            }
        }
    }

    fun setOnIconClickListener(onClick: () -> Unit) {
        vb.ivActionIcon.setOnClickListener {
            reset()
            onClick.invoke()
        }
    }

    fun setActionIconVisibility(isVisible: Boolean) {
        vb.ivActionIcon.isVisible = isVisible
    }
}