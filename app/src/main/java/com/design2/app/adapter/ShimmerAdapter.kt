package com.design2.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.R
import com.design2.app.databinding.ItemSimpleTextBinding
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setHorizontalMargin
import com.design2.chili2.view.adapter.BaseAdapter
import com.design2.chili2.view.adapter.BaseViewHolder

class ShimmerAdapter(private val context: Context): BaseAdapter<String>(context, loadingItemCount = 5) {
    override fun createItemViewHolder(parent: ViewGroup): BaseViewHolder<String?> {
        val view = ItemSimpleTextBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_simple_text, parent, false))
        return ItemViewHolder(view)
    }

    override fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: String?) {
        if (holder is ItemViewHolder) holder.onBind(item)
    }

    class ItemViewHolder(private val binding: ItemSimpleTextBinding) : BaseViewHolder<String?>(binding.root) {
        override fun onBind(item: String?, isLast: Boolean) {
            binding.tvItem.text = item
            binding.root.apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setHorizontalMargin(resources.getDimension(com.design2.chili2.R.dimen.padding_16dp).toInt())
                setBottomMargin(resources.getDimension(com.design2.chili2.R.dimen.padding_8dp).toInt())
            }
        }
    }

}