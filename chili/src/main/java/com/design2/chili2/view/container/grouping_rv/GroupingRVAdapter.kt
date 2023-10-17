package com.design2.chili2.view.container.grouping_rv

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class GroupingRVAdapter(
    private val adapterCreator: (Int) -> GroupableAdapter,
    private val emptyVHCreator: (ViewGroup) -> BaseGroupingVH
) : ListAdapter<GroupingItem, BaseGroupingVH>(GroupingAdapterDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseGroupingVH {
        return when (viewType) {
            SHADOW_GROUP -> ShadowGroupVH.create(parent, adapterCreator.invoke(viewType))
            NON_SHADOW_GROUP -> NonShadowGroupVH.create(parent, adapterCreator.invoke(viewType))
            else -> emptyVHCreator.invoke(parent)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: BaseGroupingVH, position: Int) {
        holder.bind(currentList.getOrNull(position))
    }

    override fun getItemViewType(position: Int): Int {
        return currentList.getOrNull(position)?.getItemType() ?: -1
    }

    companion object {
        const val SHADOW_GROUP = 1
        const val NON_SHADOW_GROUP = 2
    }
}

object GroupingAdapterDiffUtil : DiffUtil.ItemCallback<GroupingItem>() {
    override fun areItemsTheSame(oldItem: GroupingItem, newItem: GroupingItem): Boolean {
        return oldItem.isItemsSame(newItem)
    }

    override fun areContentsTheSame(oldItem: GroupingItem, newItem: GroupingItem): Boolean {
        return oldItem.isContentsSame(newItem)
    }
}