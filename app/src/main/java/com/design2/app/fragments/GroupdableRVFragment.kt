package com.design2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentGroupableRvBinding
import com.design2.app.databinding.ItemEmptyPoleItemBinding
import com.design2.app.databinding.ItemPoleItemBinding
import com.design2.chili2.view.container.grouping_rv.BaseGroupingVH
import com.design2.chili2.view.container.grouping_rv.GroupableAdapter
import com.design2.chili2.view.container.grouping_rv.GroupingItem
import com.design2.chili2.view.container.grouping_rv.GroupingRVAdapter
import com.design2.chili2.view.container.grouping_rv.ShadowGroupItems
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering

class GroupdableRVFragment : BaseFragment<FragmentGroupableRvBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.rv.adapter = GroupingRVAdapter(
            { ItemsAdapter() },
            { EmptyVH.create(it) }
        ).apply {
            submitList(
                listOf(
                    ShadowGroupItems(listOf(
                        PoleItem("1"),
                        PoleItem("2"),
                        PoleItem("3"),
                        PoleItem("4"),
                        PoleItem("5"),
                        PoleItem("6"),
                        PoleItem("7"),
                    )),
                    ShadowGroupItems(listOf(
                        PoleItem("21"),
                        PoleItem("22"),
                        PoleItem("23"),
                        PoleItem("24"),
                        PoleItem("25"),
                        PoleItem("26"),
                        PoleItem("27"),
                    )),
                    ShadowGroupItems(listOf(
                        PoleItem("31"),
                        PoleItem("32"),
                        PoleItem("33"),
                        PoleItem("34"),
                        PoleItem("35"),
                        PoleItem("36"),
                        PoleItem("37"),
                    )),
                    ShadowGroupItems(listOf(
                        PoleItem("41"),
                        PoleItem("42"),
                        PoleItem("43"),
                        PoleItem("44"),
                        PoleItem("45"),
                        PoleItem("46"),
                        PoleItem("47"),
                    ))
                )
            )
        }
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

class ItemsAdapter : RecyclerView.Adapter<PoleItemVH>(), GroupableAdapter {

    private val items = mutableListOf<PoleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoleItemVH {
        return PoleItemVH.create(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PoleItemVH, position: Int) {
        holder.onBind(items[position], position == 0, items.lastIndex == position)
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

}

class PoleItemVH(val vb: ItemPoleItemBinding) : RecyclerView.ViewHolder(vb.root) {

    fun onBind(item: PoleItem, isFirst: Boolean, isLast: Boolean) {
        vb.bcv.setTitle(item.title)
        vb.bcv.setupRoundedModeByPosition(isFirst, isLast)
    }

    companion object {
        fun create(parent: ViewGroup): PoleItemVH {
            val view = ItemPoleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PoleItemVH(view)
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