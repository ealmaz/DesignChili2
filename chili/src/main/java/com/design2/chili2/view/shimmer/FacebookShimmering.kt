package com.design2.chili2.view.shimmer

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

interface FacebookShimmering {

    fun onStartShimmer()

    fun onStopShimmer()

    fun getShimmerLayouts(): List<ShimmerFrameLayout>

    // Map of actual view to shimmer view
    fun getShimmeribleViewsPair(): Map<View, View?>

}