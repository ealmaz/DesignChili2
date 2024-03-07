package com.design2.chili2.view.stories

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.design2.chili2.databinding.ChiliViewStoriesBinding
import com.design2.chili2.view.stories.adapter.PageTransformer
import com.design2.chili2.view.stories.adapter.StoryPagerAdapter

class StoriesView : ConstraintLayout {
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

    private var storyViewPagerAdapter: StoryPagerAdapter? = null

    private var viewPager: ViewPager2? = null

    private fun init(context: Context) {
        binding = ChiliViewStoriesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupStories(list: List<StoryBlock>, fragmentActivity: FragmentActivity, listener: StoryListener) {
        viewPager = binding.viewPager.apply {
            setPageTransformer(PageTransformer())
            viewPager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        storyViewPagerAdapter?.pauseAllFragments()
                    }
                }
            })
        }

        storyViewPagerAdapter = StoryPagerAdapter(fragmentActivity)

        storyViewPagerAdapter?.createViewPager(stories = list, listener)

        viewPager?.adapter = storyViewPagerAdapter
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