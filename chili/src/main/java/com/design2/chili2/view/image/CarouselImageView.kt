package com.design2.chili2.view.image

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.design2.chili2.R

class CarouselImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {
    private val handler = Handler()
    private val autoScrollDelay: Long = 3000
    private var currentPage = 0

    private val pageSwitcher = object : Runnable {
        override fun run() {
            if (adapter != null) {
                currentPage = if (currentPage < adapter!!.count - 1) currentPage + 1 else 0
                setCurrentItem(currentPage, true)
                handler.postDelayed(this, autoScrollDelay)
            }
        }
    }

    init {
        offscreenPageLimit = 3

        val pageMarginPx = resources.getDimensionPixelSize(R.dimen.padding_16dp)
        val viewPagerPadding = resources.getDimensionPixelSize(R.dimen.padding_32dp)

        pageMargin = pageMarginPx
        setPadding(viewPagerPadding, 0, viewPagerPadding, 0)
        clipToPadding = false
        clipChildren = false
        isSaveEnabled = true

        (parent as? ViewGroup)?.clipChildren = false

        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
                when(position) {
                    0 -> {
                        setPadding(viewPagerPadding / 2, 0, viewPagerPadding + pageMarginPx, 0)
                    }
                    adapter!!.count - 1 -> {
                        setPadding(viewPagerPadding + pageMarginPx, 0, viewPagerPadding / 2, 0)
                    }
                    else -> {
                        setPadding(viewPagerPadding, 0, viewPagerPadding, 0)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == SCROLL_STATE_IDLE) {
                    handler.removeCallbacks(pageSwitcher)
                    handler.postDelayed(pageSwitcher, autoScrollDelay)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    fun startAutoScroll() {
        handler.postDelayed(pageSwitcher, autoScrollDelay)
    }

    fun stopAutoScroll() {
        handler.removeCallbacks(pageSwitcher)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAutoScroll()
    }

    override fun onDetachedFromWindow() {
        stopAutoScroll()
        super.onDetachedFromWindow()
    }
}
