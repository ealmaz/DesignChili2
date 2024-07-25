package kg.devcats.chili3.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpacesDecoration(
    private val divider: Int,
    private val outerDivider: Int,
    private val verticalDivider: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager: GridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanCount: Int = layoutManager.spanCount

        val viewHolder = parent.getChildViewHolder(view)
        val itemCount = parent.adapter?.itemCount ?: return
        val adapterLastIndex = itemCount - 1
        val itemPosition =
            viewHolder.bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                ?: viewHolder.oldPosition

        val oneSizeDivider = divider / 2

        fun isLastItemInColumn(isEvenNumberOfItems: Boolean): Boolean {
            val fromStartIndex = when (isEvenNumberOfItems) {
                true -> itemCount - spanCount
                else -> itemCount - spanCount + 1
            }
            return itemPosition in fromStartIndex..adapterLastIndex
        }

        outRect.left = if (itemPosition < spanCount) outerDivider else oneSizeDivider
        outRect.right = when (isLastItemInColumn(itemCount % spanCount == 0)) {
            true -> outerDivider
            else -> oneSizeDivider
        }

        outRect.bottom = verticalDivider
    }

}