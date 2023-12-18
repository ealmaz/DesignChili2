package com.design2.chili2.view.container.grouping_rv

interface GroupingItem {

    fun getItemType(): Int?

    fun isItemsSame(newItem: GroupingItem): Boolean
    fun isContentsSame(newItem: GroupingItem): Boolean

    fun setItemStateMode(itemsStateMode: ItemsStateMode) {}
    fun getItemStateMode(): ItemsStateMode = ItemsStateMode.DEFAULT

    fun getChildItems() : List<GroupingItem> = emptyList()
}

enum class ItemsStateMode {
    DEFAULT, EDITING
}