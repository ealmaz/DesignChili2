package com.design2.chili2.view.cells.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.image.SquircleView

class MultiIconedAdapter(var listener: (() -> Unit)? = null) :
    RecyclerView.Adapter<MultiIconedAdapter.IconVH>() {

    private val icons = ArrayList<Pair<Any, Int>>()

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

    fun addIcons(icons: ArrayList<String>, pixelSize: Int) {
        this.icons.clear()
        icons.forEach {
            this.icons.add(it to pixelSize)
        }
        notifyDataSetChanged()
    }

    fun addShimmerIcons(icons: ArrayList<Int>, pixelSize: Int) {
        this.icons.clear()
        icons.forEach {
            this.icons.add(it to pixelSize)
        }
        notifyDataSetChanged()
    }

    inner class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(image: Any, pixelSize: Int) {
            val ivImg = itemView.findViewById<SquircleView>(R.id.iv_img)
            ivImg.apply {
                when(image) {
                    is String -> setImageByUrl(image)
                    is Int -> setImageResource(image)
                }
                setOnSingleClickListener { listener?.invoke() }
                layoutParams.apply {
                    height = pixelSize
                    width = pixelSize
                }
            }
        }
    }
}