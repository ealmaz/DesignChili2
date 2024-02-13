package com.design2.chili2.view.container.grouping_rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.databinding.ChiliItemNonShadowGroupBinding

class NonShadowPagerGroupVH(val holderVb: ChiliItemNonShadowGroupBinding) : BaseGroupingVH(holderVb.root) {

    private lateinit var adapter: GroupablePagerAdapter

    override fun bind(item: GroupingItem?) {
        super.bind(item)
        adapter.setItemsStateMode(item?.getItemStateMode() ?: ItemsStateMode.DEFAULT)
        adapter.setItems((item as NonShadowPagerGroupItems).items)
    }

    override fun clearPrevState() {
        adapter.clearItems()
    }

    companion object {
        fun create(parent: ViewGroup, adapter: GroupablePagerAdapter): NonShadowPagerGroupVH {
            val vb = ChiliItemNonShadowGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NonShadowPagerGroupVH(vb).apply {
                this.adapter = adapter
                vb.rvItems.adapter = adapter as? RecyclerView.Adapter<*>
            }
        }
    }
}

data class NonShadowPagerGroupItems(val items: PagingData<GroupingItem>,
                               var itemsStateMode: ItemsStateMode = ItemsStateMode.DEFAULT): GroupingItem {

    override fun getItemType(): Int {
        return GroupingPagingAdapter.NON_SHADOW_GROUP
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? NonShadowPagerGroupItems) != null
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? NonShadowPagerGroupItems)?.equals(this) == true
    }

    override fun setItemStateMode(itemsStateMode: ItemsStateMode) {
        this.itemsStateMode = itemsStateMode
    }

    override fun getItemStateMode(): ItemsStateMode {
        return this.itemsStateMode
    }
}