package com.design2.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentGroupableRvBinding
import com.design2.app.databinding.ItemEmptyPoleItemBinding
import com.design2.app.databinding.ItemPoleItemBinding
import com.design2.chili2.extensions.setOnDragDropListener
import com.design2.chili2.view.container.grouping_rv.BaseGroupingVH
import com.design2.chili2.view.container.grouping_rv.GroupableAdapter
import com.design2.chili2.view.container.grouping_rv.GroupingItem
import com.design2.chili2.view.container.grouping_rv.GroupingRVAdapter
import com.design2.chili2.view.container.grouping_rv.ItemsStateMode
import com.design2.chili2.view.container.grouping_rv.ShadowGroupItems
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import java.util.Collections

class GroupdableRVFragment : BaseFragment<FragmentGroupableRvBinding>() {

    lateinit var adapterL: ListAdapter<GroupingItem, BaseGroupingVH>
    var isEddtin = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.btn.setOnClickListener {
            isEddtin = !isEddtin
            submit(isEddtin)
        }
        adapterL = GroupingRVAdapter(
            { ItemsAdapter() },
            { EmptyVH.create(it) }
        )
        vb.rv.adapter = adapterL
        submit(false)
    }

    private fun submit(isEditing: Boolean) {
        adapterL.submitList(
            listOf(
                ShadowGroupItems(listOf(
                    PoleItem("1"),
                    PoleItem("2"),
                    PoleItem("3"),
                    PoleItem("4"),
                    PoleItem("5"),
                    PoleItem("6"),
                    PoleItem("7"),
                )).apply {
                    if (isEditing) setItemStateMode(ItemsStateMode.EDITING)
                },
                ShadowGroupItems(listOf(
                    PoleItem("21"),
                    PoleItem("22"),
                    PoleItem("23"),
                    PoleItem("24"),
                    PoleItem("25"),
                    PoleItem("26"),
                    PoleItem("27"),
                )).apply {
                    if (isEditing) setItemStateMode(ItemsStateMode.EDITING)
                },
                ShadowGroupItems(listOf(
                    PoleItem("31"),
                    PoleItem("32"),
                    PoleItem("33"),
                    PoleItem("34"),
                    PoleItem("35"),
                    PoleItem("36"),
                    PoleItem("37"),
                )).apply {
                    if (isEditing) setItemStateMode(ItemsStateMode.EDITING)
                },
                ShadowGroupItems(listOf(
                    PoleItem("41 - Non editable"),
                    PoleItem("42 - Non editable"),
                    PoleItem("43 - Non editable"),
                    PoleItem("44 - Non editable"),
                    PoleItem("45 - Non editable"),
                    PoleItem("46 - Non editable"),
                    PoleItem("47 - Non editable"),
                ))
            )
        )
    }

    override fun inflateViewBinging(): FragmentGroupableRvBinding {
        return FragmentGroupableRvBinding.inflate(layoutInflater)
    }

    override fun startShimmering() {
        super.startShimmering()
        vb.root.startGroupShimmering()
    }

    override fun stopShimmering() {
        super.stopShimmering()
        vb.root.stopGroupShimmering()
    }
}

class ItemsAdapter : RecyclerView.Adapter<PoleItemVH>(), GroupableAdapter, DragAndDropCallbackListener, PoleItemAdapterListener {

    var touchHelper: ItemTouchHelper? = ItemTouchHelper(DragAndDropCallback(this))

    private lateinit var state: ItemsStateMode

    override fun setItemsStateMode(itemsStateMode: ItemsStateMode) {
        super.setItemsStateMode(itemsStateMode)
        state = itemsStateMode

    }

    private val items = mutableListOf<PoleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoleItemVH {
        return PoleItemVH.create(parent, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PoleItemVH, position: Int) {
        holder.onBind(items[position], position == 0, items.lastIndex == position, state == ItemsStateMode.EDITING)
    }

    override fun setItems(items: List<out GroupingItem?>) {
        this.items.clear()
        items.forEach {
            this.items.add(it as PoleItem)
        }
        notifyDataSetChanged()
    }

    override fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }


    override fun swapItems(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) Collections.swap(items, i, i + 1)
        } else {
            for (i in fromPosition..toPosition + 1) Collections.swap(items, i, i - 1)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun areItemsMovable(
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun getItemTouchHelper(): ItemTouchHelper? {
        return touchHelper
    }

    override fun startDragging(viewHolder: PoleItemVH) {
        touchHelper?.startDrag(viewHolder)
    }

}

data class PoleItem(
    val title: String
): GroupingItem {

    override fun getItemType(): Int? {
        return null
    }

    override fun isItemsSame(newItem: GroupingItem): Boolean {
        return (newItem as? PoleItem) != null
    }

    override fun isContentsSame(newItem: GroupingItem): Boolean {
        return (newItem as? PoleItem)?.equals(this) == true
    }

    override fun getChildItems(): List<GroupingItem> {
        return emptyList()
    }
}

class PoleItemVH(val vb: ItemPoleItemBinding) : RecyclerView.ViewHolder(vb.root) {

    var listener: PoleItemAdapterListener? = null

    fun onBind(item: PoleItem, isFirst: Boolean, isLast: Boolean, isEditing: Boolean) {
        if (isEditing) vb.bcv.setTitle("Editing mode for " + item.title)
        else vb.bcv.setTitle(item.title)
        vb.bcv.setupRoundedModeByPosition(isFirst, isLast)
        vb.bcv.findViewById<ImageView>(com.design2.chili2.R.id.iv_chevron).setOnDragDropListener {
            listener?.startDragging(this)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: PoleItemAdapterListener): PoleItemVH {
            val view = ItemPoleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PoleItemVH(view).apply {
                this.listener = listener
            }
        }
    }

}

class EmptyVH(view: View) : BaseGroupingVH(view) {

    override fun clearPrevState() {}

    companion object {
        fun create(parent: ViewGroup): EmptyVH {
            val view = ItemEmptyPoleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EmptyVH(view.root)
        }
    }
}

class DragAndDropCallback(private val listener: DragAndDropCallbackListener): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return if (listener.areItemsMovable(viewHolder, target)) {
            listener.swapItems(viewHolder, target)
            true
        } else {
            false
        }
    }

    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        listener.onItemsMoved(viewHolder, target)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    override fun isLongPressDragEnabled() = false
    override fun isItemViewSwipeEnabled() = false

}

interface PoleItemAdapterListener {
    fun startDragging(viewHolder: PoleItemVH)
}

interface DragAndDropCallbackListener {
    fun swapItems(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
    fun areItemsMovable(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
    fun onItemsMoved(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = Unit
}