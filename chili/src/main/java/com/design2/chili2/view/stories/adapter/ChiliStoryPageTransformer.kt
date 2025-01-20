package com.design2.chili2.view.stories.adapter

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ChiliStoryPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val deltaY = 0.5F

        if (position >= -1 && position <= 1) {
            view.pivotX = if (position < 0F) view.width.toFloat() else 0F
            view.pivotY = view.height * deltaY
            view.rotationY = 45F * position
        } else {
            view.rotationY = 0F
        }
    }
}
