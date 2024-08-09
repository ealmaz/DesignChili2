package kg.devcats.chili3.view.common.chips

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import kg.devcats.chili3.databinding.ChiliViewSimpleTextChipBinding
import kg.devcats.chili3.extensions.getColorFromAttr

class ChiliSimpleTextChipVH : ChiliChipViewHolder() {

    private lateinit var vb: ChiliViewSimpleTextChipBinding

    override fun createView(context: Context): View {
        vb = ChiliViewSimpleTextChipBinding.inflate(LayoutInflater.from(context))
        return vb.root
    }

    override fun setupAsSelected() {
        super.setupAsSelected()
        vb.root.apply {
            isSelected = true
            setTextColor(Color.WHITE)
        }
    }

    override fun setupAsUnselected() {
        super.setupAsUnselected()
        vb.root.apply {
            isSelected = false
            setTextColor(context.getColorFromAttr(com.design2.chili2.R.attr.ChiliPrimaryTextColor))
        }
    }

    override fun onBind(data: SelectableItemData?) {
        super.onBind(data)
        vb.root.text = (data as? SimpleTextChip)?.text
    }
}

data class SimpleTextChip(
    override val itemId: Int,
    val text: String
): SelectableItemData