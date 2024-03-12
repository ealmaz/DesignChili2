package com.design2.chili2.view.stories

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.design2.chili2.databinding.ChiliViewStoriesBinding
import com.design2.chili2.view.stories.adapter.ChiliStoryPageTransformer
import com.design2.chili2.view.stories.adapter.ChilliStoryPagerAdapter

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

    private fun init(context: Context) {
        binding = ChiliViewStoriesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupStories(list: List<ChilliStoryBlock>, fragmentActivity: FragmentActivity, listener: StoryListener) {
        viewPager = binding.viewPager.apply {
            setPageTransformer(ChiliStoryPageTransformer())
            offscreenPageLimit = 3
        }

        storyViewPagerAdapter = ChilliStoryPagerAdapter(fragmentActivity)

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