package com.design2.chili2.view.shimmer

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.visible

fun ShimmeringView.startShimmering() {
    onStartShimmer()
    (this as? ViewGroup)?.setIsSurfaceClickable(false)
    getShimmeringViewsPair().forEach {
        it.key.invisible()
        it.value?.apply {
            visible()
            startShimmer()
        }
    }
}

fun ShimmeringView.stopShimmering() {
    onStopShimmer()
    (this as? ViewGroup)?.setIsSurfaceClickable(true)
    getShimmeringViewsPair().forEach {
        it.key.visible()
        it.value?.apply {
            stopShimmer()
            gone()
        }
    }
}

fun ViewGroup.setupShimmeringForViewGroup(isShimmering: Boolean) {
    children.forEach {
        it.setupSimmering(isShimmering)
    }
}

fun View.setupSimmering(isShimmering: Boolean) {
    if (this is ShimmeringView) {
        if (isShimmering) this.startShimmering()
        else this.stopShimmering()
    } else {
        (this as? ViewGroup)?.children?.forEach {
            it.setupSimmering(isShimmering)
        }
    }
}