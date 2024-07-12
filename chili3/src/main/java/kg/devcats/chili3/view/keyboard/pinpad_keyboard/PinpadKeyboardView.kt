package kg.devcats.chili3.view.keyboard.pinpad_keyboard

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliPinpadKeyboardBinding

class PinpadKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.pinpadKeyboardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_PinpadKeyboardDefaultStyle,
) : FrameLayout(context, attrs) {

    private val vb = ChiliPinpadKeyboardBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.PinpadKeyboardView, defStyleAttr, defStyleRes).run {
            setIsLeftActionEnabled(getBoolean(R.styleable.PinpadKeyboardView_leftActionEnabled, true))
            setLeftActionText(getString(R.styleable.PinpadKeyboardView_leftActionText))
            getResourceId(R.styleable.PinpadKeyboardView_leftActionTextAppearance, -1).takeIf { it != -1 }
                ?.let { setLeftActionTextAppearance(it) }
            getResourceId(R.styleable.PinpadKeyboardView_leftActionDrawable, -1).takeIf { it != -1 }
                ?.let { setLeftActionDrawable(it) }
            setLeftActionDrawableSize(
                getDimensionPixelSize(R.styleable.PinpadKeyboardView_leftActionDrawableWidth, -1).takeIf { it != -1 },
                getDimensionPixelSize(R.styleable.PinpadKeyboardView_leftActionDrawableHeight, -1).takeIf { it != -1 }
            )
            setLeftActionClickableMaskEnebled(getBoolean(R.styleable.PinpadKeyboardView_leftActionClickableMaskEnabled, true))
            setIsRightActionEnabled(getBoolean(R.styleable.PinpadKeyboardView_rightActionEnabled, true))
            setRightActionText(getString(R.styleable.PinpadKeyboardView_rightActionText))
            getResourceId(R.styleable.PinpadKeyboardView_rightActionTextAppearance, -1).takeIf { it != -1 }
                ?.let { setRightActionTextAppearance(it) }
            getResourceId(R.styleable.PinpadKeyboardView_rightActionDrawable, -1).takeIf { it != -1 }
                ?.let { setRightActionDrawable(it) }
            setRightActionDrawableSize(
                getDimensionPixelSize(R.styleable.PinpadKeyboardView_rightActionDrawableWidth, -1).takeIf { it != -1 },
                getDimensionPixelSize(R.styleable.PinpadKeyboardView_rightActionDrawableHeight, -1).takeIf { it != -1 }
            )
            setRightActionClickableMaskEnebled(getBoolean(R.styleable.PinpadKeyboardView_rightActionClickableMaskEnabled, true))
            recycle()
        }
    }

    fun setIsLeftActionEnabled(isEnabled: Boolean) {
        vb.btnLeftAction.isInvisible = !isEnabled
    }

    fun setLeftActionText(text: CharSequence?) {
        vb.btnLeftAction.setButtonText(text)
    }

    fun setLeftActionText(textRes: Int?) {
        vb.btnLeftAction.setButtonText(textRes)
    }

    fun setLeftActionTextAppearance(resId: Int) {
        vb.btnLeftAction.setButtonTextTextAppearance(resId)
    }

    fun setLeftActionDrawable(drawable: Drawable) {
        vb.btnLeftAction.setButtonImage(drawable)
    }

    fun setLeftActionDrawable(drawableRes: Int) {
        vb.btnLeftAction.setButtonImage(drawableRes)
    }

    fun setLeftActionDrawableSize(widthPx: Int?, heightPx: Int?) {
        vb.btnLeftAction.setDrawableSize(widthPx, heightPx)
    }

    fun setLeftActionClickableMaskEnebled(isEnabled: Boolean) {
        vb.btnLeftAction.setIsClickableMaskEnabled(isEnabled)
    }

    fun setIsRightActionEnabled(isEnabled: Boolean) {
        vb.btnRightAction.isInvisible = !isEnabled
    }

    fun setRightActionText(text: CharSequence?) {
        vb.btnRightAction.setButtonText(text)
    }

    fun setRightActionText(textRes: Int?) {
        vb.btnRightAction.setButtonText(textRes)
    }

    fun setRightActionTextAppearance(resId: Int) {
        vb.btnRightAction.setButtonTextTextAppearance(resId)
    }

    fun setRightActionDrawable(drawable: Drawable) {
        vb.btnRightAction.setButtonImage(drawable)
    }

    fun setRightActionDrawable(drawableRes: Int) {
        vb.btnRightAction.setButtonImage(drawableRes)
    }

    fun setRightActionDrawableSize(widthPx: Int?, heightPx: Int?) {
        vb.btnRightAction.setDrawableSize(widthPx, heightPx)
    }

    fun setRightActionClickableMaskEnebled(isEnabled: Boolean) {
        vb.btnRightAction.setIsClickableMaskEnabled(isEnabled)
    }

    fun setPinpadEnterListener(listener: PinpadClickListener) = with(vb) {
        btn0.setOnClickListener { listener.onDigitClick(0) }
        btn1.setOnClickListener { listener.onDigitClick(1) }
        btn2.setOnClickListener { listener.onDigitClick(2) }
        btn3.setOnClickListener { listener.onDigitClick(3) }
        btn4.setOnClickListener { listener.onDigitClick(4) }
        btn5.setOnClickListener { listener.onDigitClick(5) }
        btn6.setOnClickListener { listener.onDigitClick(6) }
        btn7.setOnClickListener { listener.onDigitClick(7) }
        btn8.setOnClickListener { listener.onDigitClick(8) }
        btn9.setOnClickListener { listener.onDigitClick(9) }
        btnLeftAction.setOnClickListener { listener.onLeftActionClick() }
        btnRightAction.setOnClickListener { listener.onRightActionClick() }
    }
}

interface PinpadClickListener {

    fun onDigitClick(digit: Int)
    fun onLeftActionClick()
    fun onRightActionClick()

}