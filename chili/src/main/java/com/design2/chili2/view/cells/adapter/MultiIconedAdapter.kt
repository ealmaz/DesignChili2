package com.design2.chili2.view.cells.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.util.IconSize
import com.design2.chili2.view.image.SquircleView

class MultiIconedAdapter(var listener: (() -> Unit)? = null) :
    RecyclerView.Adapter<MultiIconedAdapter.IconVH>() {

    private val icons = ArrayList<Pair<Int, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconVH {
        return IconVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chili_item_icon, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IconVH, position: Int) {
        holder.bind(icons[position].second, icons[position].first)
    }

    override fun getItemCount(): Int = icons.size

    fun addIcons(icons: ArrayList<String>, mode: IconSize = IconSize.MEDIUM) {
        this.icons.clear()
        icons.forEach {
            this.icons.add(mode.value to it)
        }
        notifyDataSetChanged()
    }

    inner class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: String, iconSize: Int) {
            val ivImg = itemView.findViewById<SquircleView>(R.id.iv_img)
            ivImg.apply {
                setImageByUrl(item)
                setOnSingleClickListener { listener?.invoke() }
                layoutParams.apply {
                    height = calculateSize(iconSize)
                    width = calculateSize(iconSize)
                }
            }
        }

        private fun calculateSize(iconSize: Int): Int {
            return when (iconSize) {
                IconSize.MEDIUM.value -> 24.dp
                IconSize.LARGE.value -> 30.dp
                else -> 24.dp
            }
        }
    }
}