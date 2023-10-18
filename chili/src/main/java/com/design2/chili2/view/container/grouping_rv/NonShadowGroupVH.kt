package com.design2.chili2.view.container.grouping_rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R

class NonShadowGroupVH(val view: View) : BaseGroupingVH(view) {

    private lateinit var adapter: GroupableAdapter

    override fun bind(item: GroupingItem?) {
        super.bind(item)
        adapter.setItemsStateMode(item?.getItemStateMode() ?: ItemsStateMode.DEFAULT)
        adapter.setItems((item as NonShadowGroupItems).items)
    }

    override fun clearPrevState() {
        adapter.clearItems()
    }

    companion object {
        fun create(parent: ViewGroup, adapter: GroupableAdapter): NonShadowGroupVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chili_item_non_shadow_group, parent, false)
            return NonShadowGroupVH(view).apply {
                this.adapter = adapter
                view.findViewById<RecyclerView>(R.id.rv_items).adapter = adapter as? RecyclerView.Adapter<*>
            }
        }
    }
}

data class NonShadowGroupItems(val items: List<GroupingItem>,
                               var itemsStateMode: ItemsStateMode = ItemsStateMode.DEFAULT): GroupingItem {

    override fun getItemType(): Int {
        return GroupingRVAdapter.NON_SHADOW_GROUP
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? NonShadowGroupItems) != null
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? NonShadowGroupItems)?.equals(this) == true
    }

    override fun setItemStateMode(itemsStateMode: ItemsStateMode) {
        this.itemsStateMode = itemsStateMode
    }

    override fun getItemStateMode(): ItemsStateMode {
        return this.itemsStateMode
    }
}