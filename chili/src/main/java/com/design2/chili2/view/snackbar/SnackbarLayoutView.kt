package com.design2.chili2.view.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import com.design2.chili2.R
import com.google.android.material.snackbar.ContentViewCallback

class SnackbarLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ContentViewCallback {

    var pbProgress: ProgressBar
    var tvMessage: TextView
    var tvSecondsLeft: TextView
    var ivIcon: ImageView
    var tvAction: TextView
    var rootView: CardView

    init {
        View.inflate(context, R.layout.chili_view_snackbar_layout, this)
        this.pbProgress = findViewById(R.id.pb_progress)
        this.tvMessage = findViewById(R.id.tv_message)
        this.tvSecondsLeft = findViewById(R.id.tv_seconds_left)
        this.ivIcon = findViewById(R.id.iv_icon)
        this.tvAction = findViewById(R.id.tv_action)
        this.rootView = findViewById(R.id.root_view)
        clipToPadding = false
    }

    fun setCustomBackgroundColor(@ColorRes color: Int) {
        rootView.setCardBackgroundColor(resources.getColor(color, null))
    }

    override fun animateContentIn(delay: Int, duration: Int) {}
    override fun animateContentOut(delay: Int, duration: Int) {}
}