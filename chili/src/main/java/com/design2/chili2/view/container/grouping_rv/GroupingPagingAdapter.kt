package com.design2.chili2.view.container.grouping_rv

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

open class GroupingPagingAdapter(
    private val adapterCreator: (Int) -> GroupablePagerAdapter,
    private val emptyVHCreator: (ViewGroup) -> BaseGroupingVH
) : PagingDataAdapter<GroupingItem, BaseGroupingVH>(GroupingPagerAdapterDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseGroupingVH {
        return when (viewType) {
            SHADOW_GROUP -> ShadowPagerGroupVH.create(parent, adapterCreator.invoke(viewType))
            NON_SHADOW_GROUP -> NonShadowPagerGroupVH.create(parent, adapterCreator.invoke(viewType))
            else -> emptyVHCreator.invoke(parent)
        }
    }

    override fun getItemCount(): Int {
        return snapshot().size
    }

    override fun onBindViewHolder(holder: BaseGroupingVH, position: Int) {
        holder.bind(snapshot().getOrNull(position))
    }

    override fun getItemViewType(position: Int): Int {
        return snapshot().getOrNull(position)?.getItemType() ?: -1
    }

    companion object {
        const val SHADOW_GROUP = 1
        const val NON_SHADOW_GROUP = 2
    }
}

object GroupingPagerAdapterDiffUtil : DiffUtil.ItemCallback<GroupingItem>() {
    override fun areItemsTheSame(oldItem: GroupingItem, newItem: GroupingItem): Boolean {
        return oldItem.isItemsSame(newItem)
    }

    override fun areContentsTheSame(oldItem: GroupingItem, newItem: GroupingItem): Boolean {
        return oldItem.isContentsSame(newItem)
    }
}