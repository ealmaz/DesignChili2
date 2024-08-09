package kg.devcats.chili3.view.common.chips

import android.content.Context
import android.view.View

abstract class ChiliChipViewHolder {

    open var id: Int = -1
    open var isSelected: Boolean = false

    abstract fun createView(context: Context): View

    open fun onBind(data: SelectableItemData?) {
        if (data == null) return
        id = data.itemId
    }

    open fun setupAsSelected() { isSelected = true }

    open fun setupAsUnselected() { isSelected = false }

}