package kg.devcats.chili3.view.keyboard.pinpad_keyboard

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliPinpadButtonBinding
import kg.devcats.chili3.extensions.visible

class PinpadButtonView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private var vb: ChiliPinpadButtonBinding

    init {
        vb = ChiliPinpadButtonBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(attributeSet, R.styleable.PinpadButtonView).run {
            setButtonText(getString(R.styleable.PinpadButtonView_text))
            getResourceId(R.styleable.PinpadButtonView_textTextAppearance, -1).takeIf { it != -1 }?.let {
                setButtonTextTextAppearance(it)
            }
            getResourceId(R.styleable.PinpadButtonView_drawable, -1).takeIf { it != -1 }?.let {
                setButtonImage(it)
            }
            setDrawableSize(
                widthPx = getDimensionPixelSize(R.styleable.PinpadButtonView_drawableWidth, -1).takeIf { it != -1 },
                heightPx = getDimensionPixelSize(R.styleable.PinpadButtonView_drawableHeight, -1).takeIf { it != -1 }
            )
            setIsClickableMaskEnabled(getBoolean(R.styleable.PinpadButtonView_clickableMaskEnabled, true))
            recycle()
        }
    }

    fun setButtonText(text: CharSequence?) {
        vb.tvText.setTextOrHide(text)
    }

    fun setButtonText(textRes: Int?) {
        vb.tvText.setTextOrHide(textRes)
    }

    fun setButtonTextTextAppearance(textAppearanceRes: Int) {
        vb.tvText.setTextAppearance(textAppearanceRes)
    }

    fun setButtonImage(drawable: Drawable?) {
        vb.ivImage.apply {
            setImageDrawable(drawable)
            visible()
        }
    }

    fun setButtonImage(drawableRes: Int) {
        vb.ivImage.apply {
            setImageResource(drawableRes)
            visible()
        }
    }

    fun setDrawableSize(widthPx: Int?, heightPx: Int?) {
        vb.ivImage.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
        }
    }

    fun setIsClickableMaskEnabled(isEnabled: Boolean) {
        vb.clickableMask.isVisible = isEnabled
    }
}