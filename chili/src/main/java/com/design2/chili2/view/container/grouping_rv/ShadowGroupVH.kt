package com.design2.chili2.view.container.grouping_rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R

@Deprecated("Vertical nested Recycler views are forbidden", ReplaceWith("ShadowLayout.updateShadowType(ShadowType.Single"))
class ShadowGroupVH(val view: View) : BaseGroupingVH(view) {

    private lateinit var adapter: GroupableAdapter

    override fun bind(item: GroupingItem?) {
        super.bind(item)
        adapter.setItemsStateMode(item?.getItemStateMode() ?: ItemsStateMode.DEFAULT)
        adapter.setItems((item as ShadowGroupItems).items)
    }

    override fun clearPrevState() {
        adapter.clearItems()
    }

    companion object {
        fun create(parent: ViewGroup, adapter: GroupableAdapter): ShadowGroupVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chili_item_shadow_group, parent, false)
            return ShadowGroupVH(view).apply {
                this.adapter = adapter
                val rv = view.findViewById<RecyclerView>(R.id.rv_items)
                rv?.let {
                    it.adapter = adapter as? RecyclerView.Adapter<*>
                    adapter.getItemTouchHelper()?.attachToRecyclerView(it)
                }
            }
        }
    }
}

data class ShadowGroupItems(val items: List<GroupingItem>,
                            var itemsStateMode: ItemsStateMode = ItemsStateMode.DEFAULT): GroupingItem {

    override fun getItemType(): Int {
        return GroupingRVAdapter.SHADOW_GROUP
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? ShadowGroupItems) != null
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? ShadowGroupItems)?.equals(this) == true
    }

    override fun setItemStateMode(itemsStateMode: ItemsStateMode) {
        this.itemsStateMode = itemsStateMode
    }

    override fun getItemStateMode(): ItemsStateMode {
        return this.itemsStateMode
    }

    override fun getChildItems(): List<GroupingItem> {
        return items
    }
}