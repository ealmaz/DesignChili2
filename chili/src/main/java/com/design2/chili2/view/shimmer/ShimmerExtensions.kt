package com.design2.chili2.view.shimmer

import android.view.ViewGroup
import androidx.core.view.children
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.visible

fun ViewGroup.startGroupShimmering() {
    (this as? ShimmeringView)?.startShimmering()
    children.forEach { (it as? ViewGroup)?.startGroupShimmering() }
}

fun ViewGroup.stopGroupShimmering() {
    (this as? ShimmeringView)?.stopShimmering()
    children.forEach { (it as? ViewGroup)?.stopGroupShimmering() }
}

fun ShimmeringView.startShimmering() {
    (this as? ViewGroup)?.setIsSurfaceClickable(false)
    onStartShimmer()
    getShimmeringViewsPair().forEach {
        it.key.invisible()
        it.value?.apply {
            visible()
            startShimmer()
        }
    }
}

fun ShimmeringView.stopShimmering(isSurfaceClickable: Boolean = true) {
    (this as? ViewGroup)?.setIsSurfaceClickable(isSurfaceClickable)
    onStopShimmer()
    getShimmeringViewsPair().forEach {
        it.key.visible()
        it.value?.apply {
            stopShimmer()
            gone()
        }
    }
}