package kg.devcats.chili3.view.cells

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.extensions.setRightMargin
import com.design2.chili2.view.cells.BaseCellView

class RadioCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toggleCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_Toggle
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var radioBtn: RadioButton? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateRadioButton()
    }

    private fun inflateRadioButton() {
        val colorStateList = ContextCompat.getColorStateList(context, kg.devcats.chili3.R.color.chili3_radio_button_color)
        this.radioBtn = RadioButton(context).apply {
            buttonTintList = colorStateList
            setButtonDrawable(kg.devcats.chili3.R.drawable.chili_radio_button_drawable)
        }
        vb.flEndPlaceHolder.addView(radioBtn as RadioButton)
        vb.flEndPlaceHolder.setRightMargin(context.resources.getDimension(R.dimen.padding_16dp).toInt())
    }



    fun setOnCheckListener(onCheck: (Boolean) -> Unit) {
        radioBtn?.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheck.invoke(isChecked)
        }
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }
}