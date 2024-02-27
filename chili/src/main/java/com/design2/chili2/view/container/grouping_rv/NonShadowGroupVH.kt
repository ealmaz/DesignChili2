package com.design2.chili2.view.container.grouping_rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.databinding.ChiliItemNonShadowGroupBinding

@Deprecated("Vertical nested Recycler views are forbidden", ReplaceWith("ShadowLayout.updateShadowType(ShadowType.Single"))
class NonShadowGroupVH(val holderVb: ChiliItemNonShadowGroupBinding) : BaseGroupingVH(holderVb.root) {

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
            val vb = ChiliItemNonShadowGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NonShadowGroupVH(vb).apply {
                this.adapter = adapter
                vb.rvItems.adapter = adapter as? RecyclerView.Adapter<*>
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

    override fun getChildItems(): List<GroupingItem> {
        return items
    }
}