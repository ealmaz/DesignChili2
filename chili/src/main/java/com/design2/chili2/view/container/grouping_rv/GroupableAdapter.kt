package com.design2.chili2.view.container.grouping_rv

interface GroupableAdapter {
    fun setItemsStateMode(itemsStateMode: ItemsStateMode) {}
    fun setItems(items: List<GroupingItem?>)
    fun clearItems()
}