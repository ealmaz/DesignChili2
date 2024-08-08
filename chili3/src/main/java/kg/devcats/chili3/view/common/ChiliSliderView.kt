package kg.devcats.chili3.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewSliderBinding

class ChiliSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = R.attr.chiliSliderViewDefaultStyle,
    defaultStyle: Int = R.style.Chili_SliderStyle
) : FrameLayout(context, attrs, defaultStyleAttr, defaultStyle) {

    private lateinit var vb: ChiliViewSliderBinding

    private var sliderValueFrom: Int = 0
    private var sliderValueTo: Int = Int.MAX_VALUE
    private var sliderStep: Int = 1

    private var displayValueFormatter: ((Int) -> CharSequence) = { it.toString() }
    private var onValueChangeListener: ((Int) -> Unit)? = null

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defaultStyleAttr, defaultStyle)
        setupView()
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewSliderBinding.inflate(LayoutInflater.from(context),  this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defaultStyleAttr: Int, defaultStyle: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ChiliSliderView, defaultStyleAttr, defaultStyle).run {
            setTitle(getString(R.styleable.ChiliSliderView_title))
            getResourceId(R.styleable.ChiliSliderView_titleTextAppearance, -1).takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
            getResourceId(R.styleable.ChiliSliderView_captionsTextAppearance, -1).takeIf { it != -1 }?.let { setCaptionsTextAppearance(it) }
            getInteger(R.styleable.ChiliSliderView_valueFrom, -1).takeIf { it != -1 }?.let { setValueFrom(it) }
            getInteger(R.styleable.ChiliSliderView_valueTo, -1).takeIf { it != -1 }?.let { setValueTo(it) }
            getInteger(R.styleable.ChiliSliderView_step, -1).takeIf { it != -1 }?.let { setStep(it) }
            getInteger(R.styleable.ChiliSliderView_sliderValue, -1).takeIf { it != -1 }?.let { setCurrentValue(it) }
            recycle()
        }
    }

    private fun setupView() = with(vb) {
        btnMinus.setOnClickListener { setCurrentValue(sliderChoosePeriod.value.toInt() - sliderStep) }
        btnPlus.setOnClickListener { setCurrentValue(sliderChoosePeriod.value.toInt() + sliderStep) }
        tvValue.text = displayValueFormatter.invoke(sliderChoosePeriod.value.toInt())
        sliderChoosePeriod.addOnChangeListener { _, value, _ ->
            tvValue.text = displayValueFormatter.invoke(value.toInt())
            onValueChangeListener?.invoke(value.toInt())
        }
        applySettings()
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.setTextOrHide(title)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setCaptionsTextAppearance(resId: Int) = with(vb) {
        tvMinusCaption.setTextAppearance(resId)
        tvPlusCaption.setTextAppearance(resId)
    }

    private fun setValueTo(maxValue: Int) {
        this.sliderValueTo = maxValue
    }

    private fun setValueFrom(minValue: Int) {
        this.sliderValueFrom = minValue
    }

    fun setCurrentValue(newValue: Int) = with(vb.sliderChoosePeriod) {
        val sliderCurrentValue = when {
            newValue % sliderStep != 0 -> sliderValueFrom
            newValue >= sliderValueTo -> sliderValueTo
            newValue <= sliderValueFrom -> sliderValueFrom
            else -> newValue
        }
        value = sliderCurrentValue.toFloat()
    }

    private fun setStep(step: Int) {
        this.sliderStep = step
    }

    fun applySettings(maxValue: Int, minValue: Int, step: Int, currentValue: Int) {
        setValueFrom(minValue)
        setValueTo(maxValue)
        setStep(step)
        applySettings()
        setCurrentValue(currentValue)
    }

    private fun applySettings() = with(vb.sliderChoosePeriod) {
        if (sliderValueTo <= sliderValueFrom) {
            valueFrom = sliderValueFrom.toFloat()
            isEnabled = false
        } else {
            isEnabled = true
            valueFrom = sliderValueFrom.toFloat()
            valueTo = sliderValueTo.toFloat()
        }
        stepSize = (sliderStep.takeIf { it > 0 } ?: 1).toFloat()
        vb.tvPlusCaption.text = displayValueFormatter.invoke(sliderValueTo)
        vb.tvMinusCaption.text = displayValueFormatter.invoke(sliderValueFrom)
    }

    fun setDisplayValueFormatter(formatter: (Int) -> CharSequence) {
        this.displayValueFormatter = formatter
        vb.tvValue.text = displayValueFormatter.invoke(vb.sliderChoosePeriod.value.toInt())
        applySettings()
    }

    fun setOnSliderValueChanged(onValueChange: (Int) -> Unit) {
        this.onValueChangeListener = onValueChange
    }
}