package kg.devcats.chili3.view.common.chips

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewChipGroupBinding
import kg.devcats.chili3.util.SelectionType

class ChiliChipsGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = R.attr.chiliChipGroupDefaultStyle,
    defStyleRes: Int = R.style.Chili_ChipGroup
) : FrameLayout(context, attrs, defaultStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewChipGroupBinding

    private var chipViewCreator: (() -> ChiliChipViewHolder) = {
        ChiliSimpleTextChipVH()
    }

    private var onSelectionChanged: ((id: Int, selected: Boolean) -> Unit)? = null

    private var selectionType = SelectionType.SINGLE

    private var chipsViewHolders = mutableListOf<ChiliChipViewHolder>()

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defaultStyleAttr, defStyleRes)
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewChipGroupBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defaultStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ChiliChipsGroup, defaultStyleAttr, defStyleRes).run {
            setTitle(getString(R.styleable.ChiliChipsGroup_title))
            getResourceId(R.styleable.ChiliChipsGroup_titleTextAppearance, -1).takeIf { it != -1 }
                ?.let { setTitleTextAppearance(it) }
            getInteger(R.styleable.ChiliChipsGroup_selectionType, -1).takeIf { it != -1 }
                ?.let { setSelectionType(it) }
            recycle()
        }
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.setTextOrHide(title)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setViewHolderCreator(creator: () -> ChiliChipViewHolder) {
        this.chipViewCreator = creator
    }

    private fun setSelectionType(type: Int) {
        this.selectionType = when (type) {
            SelectionType.SINGLE.value -> SelectionType.SINGLE
            SelectionType.MULTIPLE.value -> SelectionType.MULTIPLE
            else -> SelectionType.SINGLE
        }
    }

    fun setSelectionType(selectionType: SelectionType) {
        this.selectionType = selectionType
    }

    fun setItems(items: List<SelectableItemData>) {
        vb.llRoot.removeAllViews()
        chipsViewHolders.clear()
        items.forEach { item -> bindItem(item) }
    }

    private fun bindItem(item: SelectableItemData) {
        val viewHolder = chipViewCreator.invoke()
        val view = viewHolder.createView(context)
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            updateMargins(0, 0, 8.dp, 0)
        }
        val id = item.itemId
        viewHolder.id = id
        view.tag = id
        view.setOnClickListener(::handleClick)
        vb.llRoot.addView(view)
        viewHolder.onBind(item)
        chipsViewHolders.add(viewHolder)
    }

    private fun handleClick(view: View) {
        val id = view.tag as? Int
        val clickedVH = chipsViewHolders.find { it.id == id } ?: return
        val isSelected = clickedVH.isSelected
        if (isSelected) {
            clickedVH.setupAsUnselected()
        } else {
            unselectedPrevValuesIfNeeded()
            clickedVH.setupAsSelected()
        }
        onSelectionChanged?.invoke(id!!, !isSelected)
    }

    fun getSelectedValues(): List<Int> = chipsViewHolders.filter { it.isSelected }.map { it.id }

    fun isValueSelected(): Boolean = chipsViewHolders.any { it.isSelected }

    private fun unselectedPrevValuesIfNeeded() {
        if (selectionType != SelectionType.SINGLE) return
        chipsViewHolders.forEach { it.setupAsUnselected() }
    }

    fun setCheckedChangedListener(listener: (id: Int, selected: Boolean) -> Unit) {
        this.onSelectionChanged = listener
    }
}