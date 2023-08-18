package com.design2.chili2.view.cells.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.view.image.SquircleView

class MultiIconedShimmerAdapter : RecyclerView.Adapter<MultiIconedShimmerAdapter.IconVH>() {

    private val icons = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconVH {
        return IconVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chili_item_icon, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IconVH, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int = icons.size

    fun addIcons(icons: ArrayList<Int>) {
        this.icons.clear()
        this.icons.addAll(icons)
        notifyDataSetChanged()
    }

    inner class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Int) {
            val ivImg = itemView.findViewById<SquircleView>(R.id.iv_img)
            ivImg.setImageResource(item)
        }
    }
}