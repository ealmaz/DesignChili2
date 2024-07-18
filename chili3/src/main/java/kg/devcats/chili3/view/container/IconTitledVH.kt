package kg.devcats.chili3.view.container

import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_CROP
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.shimmer.startShimmering
import kg.devcats.chili3.view.card.IconTitledCardView

class IconTitledVH(private val view: IconTitledCardView) : RecyclerView.ViewHolder(view) {

    private lateinit var item: ExpandableGridItem

    fun onBind(item: ExpandableGridItem) {
        if (item.isShimmering) view.startShimmering()
        this.item = item
        view.setTitle(item.title)
        view.setIcon(item.icon)
    }

    companion object {
        fun create(parent: ViewGroup,  onItemClick: (String?) -> Unit): IconTitledVH {
            val view = IconTitledCardView(parent.context)
            view.layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val holder = IconTitledVH(view)
            return holder.apply {
                this.view.setScaleType(CENTER_CROP.ordinal)
                this.itemView.setOnSingleClickListener { onItemClick(item.deeplink) }
            }
        }
    }

}