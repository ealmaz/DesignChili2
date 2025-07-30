package com.design2.chili2.view.snackbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.design2.chili2.databinding.ChiliViewSnackbarLayoutBinding
import com.google.android.material.snackbar.ContentViewCallback

class SnackbarLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ContentViewCallback {

    var vb: ChiliViewSnackbarLayoutBinding

    init {
        vb = ChiliViewSnackbarLayoutBinding.inflate(LayoutInflater.from(context), this)
        clipToPadding = false
    }

    fun setCustomBackgroundColor(@ColorRes color: Int) {
        vb.rootView.setCardBackgroundColor(resources.getColor(color, null))
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val scaleX = ObjectAnimator.ofFloat(vb.pbProgress, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(vb.pbProgress, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {}
}