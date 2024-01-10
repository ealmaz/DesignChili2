package com.design2.chili2.view.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.design2.chili2.databinding.ChiliBannerCardViewBinding
import com.design2.chili2.extensions.setImageByUrl

class CarouselImageAdapter(private val images: List<String>, private val listener: Listener) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ChiliBannerCardViewBinding.inflate(LayoutInflater.from(container.context), container, false)

        binding.bannerImage.setImageByUrl(images[position])

        binding.bannerImage.setOnClickListener { listener.onBannerClicked(position) }

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    interface Listener {
        fun onBannerClicked(position: Int)
    }
}
