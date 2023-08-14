package com.design2.chili2.view.container.grouping_rv

interface GroupableAdapter {
    fun setItems(items: List<GroupingItem?>)
    fun clearItems()
}