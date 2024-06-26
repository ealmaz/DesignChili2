package com.design2.chili2.view.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliBannerCardViewBinding
import com.design2.chili2.extensions.setHorizontalMargin
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setLeftMargin
import com.design2.chili2.extensions.setRightMargin

class AutoScrollCarouselImageAdapter(private val images: List<String>, private val listener: Listener) :
    RecyclerView.Adapter<AutoScrollCarouselImageAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ChiliBannerCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String, clickListener: Listener, position: Int) {
            with(binding.bannerImage) {
                val screenWidth = context.resources.displayMetrics.widthPixels
                val fixingWidth = context.resources.getDimension(R.dimen.view_64dp).toInt() // Суммарный размер отступов с обеих сторон
                val newWidth = screenWidth - fixingWidth
                val heightPx = resources.getDimension(R.dimen.view_80dp).toInt()

                setImageByUrl(imageUrl, newWidth, heightPx)

                setOnClickListener { clickListener.onBannerClicked(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChiliBannerCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 ->
                with(holder.itemView) {
                    setLeftMargin(context.resources.getDimension(R.dimen.padding_16dp).toInt())
                    setRightMargin(context.resources.getDimension(R.dimen.padding_8dp).toInt())
                }

            itemCount - 1 ->
                with(holder.itemView) {
                    setRightMargin(context.resources.getDimension(R.dimen.padding_16dp).toInt())
                    setLeftMargin(context.resources.getDimension(R.dimen.padding_8dp).toInt())
                }

            else -> with(holder.itemView) {
                setHorizontalMargin(context.resources.getDimension(R.dimen.padding_8dp).toInt())
            }
        }
        holder.bind(images[position], listener, position)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    interface Listener {
        fun onBannerClicked(position: Int)
    }
}
