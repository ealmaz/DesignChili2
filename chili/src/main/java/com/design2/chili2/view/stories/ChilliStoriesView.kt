package com.design2.chili2.view.stories

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoriesBinding
import com.design2.chili2.view.stories.adapter.ChiliStoryPageTransformer
import com.design2.chili2.view.stories.adapter.ChilliStoryPagerAdapter
import kotlin.math.abs

class ChilliStoriesView : ConstraintLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private lateinit var binding: ChiliViewStoriesBinding

    private var storyViewPagerAdapter: ChilliStoryPagerAdapter? = null

    private var viewPager: ViewPager2? = null

    var currentFragment: ChilliStoryFragment? = null

    private lateinit var dragHelper: ViewDragHelper

    private var initialPosition: Int = 0

    private fun init(context: Context) {
        binding = ChiliViewStoriesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun setupDragViewHelper() {
        dragHelper = ViewDragHelper.create(
            findViewById(R.id.drag_frame_layout),
            1.0f,
            object : ViewDragHelper.Callback() {


                override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                    return child == binding.viewPager
                }

                override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                    return if (top < 0) 0 else top
                }

                override fun onViewPositionChanged(
                    changedView: View,
                    left: Int,
                    top: Int,
                    dx: Int,
                    dy: Int
                ) {
                    val dragOffset = top.toFloat() / binding.dragFrameLayout.height
                    val scaleFactor = 1 - dragOffset * 0.2f

//                    currentFragment?.onPause()

                    binding.viewPager.scaleX = scaleFactor
                    binding.viewPager.scaleY = scaleFactor
                }

                override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                    if (releasedChild.top > binding.root.height / 3) {
                        currentFragment?.onCloseStories()
                    }
                    else {
//                        currentFragment?.onResume()
                        resetViewPosition()
                    }
                }

                override fun getViewVerticalDragRange(child: View): Int {
                    return binding.root.height
                }
            })

        binding.dragFrameLayout.dragHelper = dragHelper
    }

    private fun resetViewPosition() {
        binding.viewPager.animate()
            .y(initialPosition.toFloat())
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(200)
            .start()
    }

    fun setupStories(list: List<ChilliStoryBlock>, fragmentActivity: FragmentActivity, onMoveListener: StoryMoveListener, onFinishListener: StoryOnFinishListener) {
        viewPager = binding.viewPager.apply {
            setPageTransformer(ChiliStoryPageTransformer())
            offscreenPageLimit = 3
        }

        storyViewPagerAdapter = ChilliStoryPagerAdapter(fragmentActivity)

        storyViewPagerAdapter?.createViewPager(stories = list, onMoveListener, onFinishListener)

        viewPager?.adapter = storyViewPagerAdapter

        viewPager?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment = storyViewPagerAdapter?.getFragmentByPosition(position)
            }
        })

        setupDragViewHelper()
    }

    fun moveToNextPage(onFinish: () -> Unit) {
        viewPager?.let {
            val currentItem = it.currentItem
            val nextItem = currentItem + 1
            if (nextItem < (it.adapter?.itemCount ?: 0))
                it.setCurrentItem(nextItem, true)
            else onFinish()
        }
    }
}