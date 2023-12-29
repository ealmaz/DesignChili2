package com.design2.chili2.view.container.grouping_rv

import androidx.annotation.StringRes
import androidx.recyclerview.widget.ItemTouchHelper

interface GroupableAdapter {
    fun setItemsStateMode(itemsStateMode: ItemsStateMode) {}
    fun setItems(items: List<GroupingItem?>)
    fun clearItems()

    fun getItemTouchHelper(): ItemTouchHelper? = null
}

data class GroupHeader(@StringRes val title: Int) : GroupingItem {
    override fun getItemType(): Int? {
        return null
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? GroupHeader) != this
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? GroupHeader)?.equals(this) == true
    }

}

data class GroupDefaultItem(val viewHolderType: Int): GroupingItem {
    override fun getItemType(): Int? {
        return null
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? GroupDefaultItem) != this
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? GroupDefaultItem)?.equals(this) == true
    }

}