package com.design2.chili2.view.container.grouping_rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

@Deprecated("Vertical nested Recycler views are forbidden", ReplaceWith("ShadowLayout.updateShadowType(ShadowType.Single"))
abstract class BaseGroupingVH(view: View): RecyclerView.ViewHolder(view) {

    open fun bind(item: GroupingItem?) {
        clearPrevState()
    }

    abstract fun clearPrevState()
}