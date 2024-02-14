package com.design2.chili2.view.cells.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.dp
import com.design2.chili2.view.image.SquircleView

class MultiIconedShimmerAdapter : RecyclerView.Adapter<MultiIconedShimmerAdapter.IconVH>() {

    private val icons = ArrayList<Pair<Int, Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconVH {
        return IconVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chili_item_icon, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IconVH, position: Int) {
        holder.bind(icons[position].first, icons[position].second)
    }

    override fun getItemCount(): Int = icons.size

    fun addIcons(icons: ArrayList<Int>, size: Int = 24) {
        this.icons.clear()
        icons.forEach {
            this.icons.add(it to size)
        }
        notifyDataSetChanged()
    }

    inner class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Int, size: Int) {
            val ivImg = itemView.findViewById<SquircleView>(R.id.iv_img)
            ivImg.apply {
                setImageResource(item)
                layoutParams.apply {
                    height = size.dp
                    width = size.dp
                }
            }
        }
    }
}