package kg.devcats.chili3.view.cells

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setRightMargin
import com.design2.chili2.view.cells.BaseCellView
import kg.devcats.chili3.R

class RadioCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.radioCellViewStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_Radio
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var radioBtn: RadioButton? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateRadioButton()
        vb.flEndPlaceHolder.setRightMargin(context.resources.getDimension(com.design2.chili2.R.dimen.padding_16dp).toInt())
    }

    fun checkRadio(needToCheck: Boolean) {
        radioBtn?.isChecked = needToCheck
    }

    fun isRadioChecked() : Boolean = radioBtn?.isChecked ?: false

    fun setOnCheckListener(onCheck: (Boolean) -> Unit) {
        radioBtn?.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheck.invoke(isChecked)
        }
        vb.root.setOnSingleClickListener {
            checkRadio(!isRadioChecked())
        }
    }

    private fun inflateRadioButton() {
        val colorStateList = ContextCompat.getColorStateList(context, R.color.chili3_radio_button_color)
        this.radioBtn = RadioButton(context).apply {
            buttonTintList = colorStateList
            setButtonDrawable(R.drawable.chili_radio_button_drawable)
        }
        vb.flEndPlaceHolder.addView(radioBtn as RadioButton)
    }
}