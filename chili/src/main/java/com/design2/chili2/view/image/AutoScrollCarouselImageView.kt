package com.design2.chili2.view.image

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import java.lang.ref.WeakReference

class AutoScrollCarouselImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private val scrollHandler by lazy {
        ScrollHandler(this)
    }

    private var delayMillis: Long

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AutoScrollImageCarouselView,
            0, 0
        ).apply {
            try {
                delayMillis = getInt(R.styleable.AutoScrollImageCarouselView_delay, 3000).toLong()
            } finally {
                recycle()
            }
        }
    }

    private fun createScroller(position: Int) = object : LinearSmoothScroller(context) {
        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_END
        }
    }.apply {
        targetPosition = position
    }

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_UP -> resumeAutoScroll()
            MotionEvent.ACTION_DOWN -> pauseAutoScroll()
        }
        parent.requestDisallowInterceptTouchEvent(true)

        return super.dispatchTouchEvent(e)
    }

    private fun pauseAutoScroll() {
        scrollHandler.removeMessages(WHAT_SCROLL)
    }

    fun resumeAutoScroll(delay: Long = 3000) {
        delayMillis = delay
        scrollHandler.removeMessages(WHAT_SCROLL)
        scrollHandler.sendEmptyMessageDelayed(WHAT_SCROLL, delayMillis)
    }

    fun scrollNext() {
        (layoutManager as? LinearLayoutManager)?.let { layoutManager ->
            val nextPosition =
                if (layoutManager.findLastVisibleItemPosition() < this.adapter?.itemCount!! - 1)
                    layoutManager.findLastVisibleItemPosition()
                else 0
            layoutManager.startSmoothScroll(
                createScroller(nextPosition)
            )
        }
        scrollHandler.sendEmptyMessageDelayed(WHAT_SCROLL, delayMillis)
    }

    private class ScrollHandler(autoScrollableRecyclerView: AutoScrollCarouselImageView) :
        Handler() {

        private val autoScrollViewPager =
            WeakReference<AutoScrollCarouselImageView>(autoScrollableRecyclerView)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            autoScrollViewPager.get()?.scrollNext()
        }
    }

    companion object {
        const val WHAT_SCROLL = 1
    }
}