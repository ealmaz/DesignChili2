package com.design2.chili2.view.container.grouping_rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R

class ShadowPagerGroupVH(val view: View) : BaseGroupingVH(view) {

    private lateinit var adapter: GroupablePagerAdapter

    override fun bind(item: GroupingItem?) {
        super.bind(item)
        adapter.setItemsStateMode(item?.getItemStateMode() ?: ItemsStateMode.DEFAULT)
        adapter.setItems((item as ShadowPagerGroupItems).items)
    }

    override fun clearPrevState() {
        adapter.clearItems()
    }

    companion object {
        fun create(parent: ViewGroup, adapter: GroupablePagerAdapter): ShadowPagerGroupVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chili_item_shadow_group, parent, false)
            return ShadowPagerGroupVH(view).apply {
                this.adapter = adapter
                val rv = view.findViewById<RecyclerView>(R.id.rv_items)
                rv?.let {
                    it.adapter = adapter as? RecyclerView.Adapter<*>
                }
            }
        }
    }
}

data class ShadowPagerGroupItems(val items: PagingData<GroupingItem>,
                            var itemsStateMode: ItemsStateMode = ItemsStateMode.DEFAULT): GroupingItem {

    override fun getItemType(): Int {
        return GroupingPagingAdapter.SHADOW_GROUP
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? ShadowPagerGroupItems) != null
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? ShadowPagerGroupItems)?.equals(this) == true
    }

    override fun setItemStateMode(itemsStateMode: ItemsStateMode) {
        this.itemsStateMode = itemsStateMode
    }

    override fun getItemStateMode(): ItemsStateMode {
        return this.itemsStateMode
    }
}