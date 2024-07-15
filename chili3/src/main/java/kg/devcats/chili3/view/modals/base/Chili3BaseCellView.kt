package kg.devcats.chili3.view.modals.base

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.view.cells.BaseCellView

class Chili3BaseCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle
) : BaseCellView(context,attrs,defStyleAttr,defStyleRes) {
    init {
        setChevronColor(kg.devcats.chili3.R.color.chili3_chevron_color)
    }

    fun setChevronColor(@ColorRes id: Int){
        vb.ivChevron.setColorFilter(ContextCompat.getColor(context, id), android.graphics.PorterDuff.Mode.MULTIPLY)
    }
}