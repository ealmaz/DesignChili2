package kg.devcats.chili3.view.container

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class IconTitledAdapter(private val onItemClick: (String?) -> Unit) : ListAdapter<ExpandableGridItem, IconTitledVH>(ServiceUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IconTitledVH.create(parent, onItemClick)

    override fun onBindViewHolder(holder: IconTitledVH, position: Int) {
        holder.onBind(getItem(position))
    }

    interface Listener {
        fun onItemClick(deeplink: String?)
    }

}

object ServiceUtil : DiffUtil.ItemCallback<ExpandableGridItem>() {
    override fun areItemsTheSame(oldItem: ExpandableGridItem, newItem: ExpandableGridItem)
            = oldItem.hashCode() == newItem.hashCode()
    override fun areContentsTheSame(oldItem: ExpandableGridItem, newItem: ExpandableGridItem)
            = oldItem == newItem
}

