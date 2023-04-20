package com.design2.chili2.view.shimmer

import android.view.View

interface Shimmering {

    fun getIgnoredViews(): Array<View>

    fun getCustomBones(): Array<CustomBone>

}