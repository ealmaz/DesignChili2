package com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetSearchSelectorItemBinding

class SearchSelectorItemVH(val vb: ChiliViewBottomSheetSearchSelectorItemBinding): RecyclerView.ViewHolder(vb.root) {

    private var item: Option? = null

    fun onBind(item: Option, isLast: Boolean) {
        this.item = item
        vb.ivChoice.isVisible = item.isSelected
        vb.tvTitle.text = item.value
        vb.divider.isVisible = !isLast
    }

    companion object{
        fun create(parent: ViewGroup, onItemSelect: (selectedId: String) -> Unit): SearchSelectorItemVH {
            val holder = SearchSelectorItemVH(
                ChiliViewBottomSheetSearchSelectorItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            return holder.apply {
                itemView.setOnClickListener {
                    item?.id?.let { onItemSelect(it) }
                }
            }
        }
    }
}

class SearchSelectorHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(item: String) {
        (itemView as TextView).text = item
    }

    companion object {

        fun create(parent: ViewGroup): SearchSelectorHeaderVH {
            val itemView = TextView(parent.context)
            itemView.run {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setPadding(resources.getDimension(R.dimen.padding_16dp).toInt(),
                        resources.getDimension(R.dimen.padding_8dp).toInt(),
                        resources.getDimension(R.dimen.padding_8dp).toInt(),
                        resources.getDimension(R.dimen.padding_8dp).toInt())
                gravity = Gravity.START
                setTextAppearance(R.style.Chili_H8_Primary)
            }
            return SearchSelectorHeaderVH(itemView)
        }
    }
}
