package com.design2.chili2.view.shimmer

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

interface ShimmeringView {

    fun onStartShimmer() {}

    fun onStopShimmer() {}

    // Map of actual view to shimmer view
    fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?>

}