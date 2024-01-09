package com.design2.chili2.view.modals

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliShowcaseLayoutBinding
import com.design2.chili2.extensions.visible

class ShowcaseHelper(private val activity: Activity) {
    fun showShowcase(targetView: View, data: ShowcaseData, onButtonClick: () -> Unit, onDismiss: () -> Unit) {
        val highlightDrawable = HighlightDrawable(targetView)

        val rootLayout = activity.findViewById<ViewGroup>(android.R.id.content)

        val showcaseView = ChiliShowcaseLayoutBinding.inflate(LayoutInflater.from(activity), rootLayout, false)

        with(showcaseView) {
            tvTooltipTitle.text = data.title
            tvTooltipText.text = data.message
            btnUnderstand.setOnClickListener {
                rootLayout.removeView(showcaseView.root)
                onButtonClick()
            }
            if (!data.buttonText.isNullOrEmpty()) {
                btnUnderstand.visible()
                btnUnderstand.text = data.buttonText
            }
            topArrowView.isVisible = data.gravity == ShowcasePosition.BOTTOM
            bottomArrowView.isVisible = data.gravity == ShowcasePosition.TOP
            if (data.indicatorsCount != null && data.indicatorsCount > 0) {
                indicatorsContainer.visible()
                createIndicators(indicatorsContainer, data.indicatorsCount, data.currentIndicatorPosition ?: 0)
            }
        }

        val statusBarHeight = getStatusBarHeight()

        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val targetLocation = IntArray(2)
                targetView.getLocationInWindow(targetLocation)

                showcaseView.cardView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showcaseView.root.setBackgroundDrawable(highlightDrawable)

                val layoutParams = showcaseView.cardView.layoutParams as ViewGroup.MarginLayoutParams

                when (data.gravity) {
                    ShowcasePosition.TOP -> {
                        layoutParams.topMargin = targetLocation[1] - statusBarHeight - showcaseView.cardView.height - showcaseView.bottomArrowView.height
                    }
                    ShowcasePosition.BOTTOM -> {
                        layoutParams.topMargin = targetLocation[1] - statusBarHeight + targetView.height + showcaseView.topArrowView.height
                    }
                }

                showcaseView.cardView.layoutParams = layoutParams
            }
        }

        showcaseView.cardView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        showcaseView.root.setOnClickListener {
            onDismiss()
            rootLayout.removeView(showcaseView.root)
        }

        rootLayout.addView(showcaseView.root)
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun createIndicators(container: LinearLayout, count: Int, currentIndex: Int) {
        container.removeAllViews()

        for (i in 0 until count) {
            val indicator = View(container.context).apply {
                val size = 8.dpToPx(container.context)
                val layoutParams = LinearLayout.LayoutParams(size, size)
                layoutParams.setMargins(4.dpToPx(container.context), 0, 4.dpToPx(container.context), 0)
                this.layoutParams = layoutParams

                this.setBackgroundResource(if (i == currentIndex) R.drawable.chili_active_indicator else R.drawable.chili_inactive_indicator)
            }
            container.addView(indicator)
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    enum class ShowcasePosition {
        TOP, BOTTOM
    }

    data class ShowcaseData(
        val title: String,
        val message: String,
        val gravity: ShowcasePosition = ShowcasePosition.BOTTOM,
        val buttonText: String? = null,
        val indicatorsCount: Int? = null,
        val currentIndicatorPosition: Int? = null
    )

    private inner class HighlightDrawable(private val targetView: View, private val isRounded: Boolean = true) : Drawable() {
        private val paint = Paint()
        private val clearPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = true
        }

        private val cornerRadius = dpToPx(if (isRounded) 12 else 0)

        init {
            paint.color = 0x88000000.toInt()
            paint.isAntiAlias = true
        }

        override fun draw(canvas: Canvas) {
            val location = IntArray(2).apply {
                targetView.getLocationInWindow(this)
            }

            location[1] -= getStatusBarHeight()

            val bitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
            val offscreenCanvas = Canvas(bitmap)

            offscreenCanvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

            val rect = RectF(
                location[0].toFloat(),
                location[1].toFloat(),
                (location[0] + targetView.width).toFloat(),
                (location[1] + targetView.height).toFloat()
            )

            val path = Path().apply {
                addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW)
            }

            offscreenCanvas.drawPath(path, clearPaint)

            canvas.drawBitmap(bitmap, 0f, 0f, null)
        }

        private fun getStatusBarHeight(): Int {
            val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId) else 0
        }

        private fun dpToPx(dp: Int): Float {
            return dp * activity.resources.displayMetrics.density
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }


}
