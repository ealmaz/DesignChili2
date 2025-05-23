package com.design2.chili2.view.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    override fun animateContentIn(delay: Int, duration: Int) {}
    override fun animateContentOut(delay: Int, duration: Int) {}
}