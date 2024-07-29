package com.design2.chili2.view.stories.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.design2.chili2.view.stories.ChilliStoryBlock
import com.design2.chili2.view.stories.ChilliStoryFragment
import com.design2.chili2.view.stories.StoryMoveListener
import com.design2.chili2.view.stories.StoryOnFinishListener

class ChilliStoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<ChilliStoryFragment>()

    fun createViewPager(stories: List<ChilliStoryBlock>, onMoveListener: StoryMoveListener, onFinishListener: StoryOnFinishListener) {
        stories.forEach {
            addFragment(ChilliStoryFragment.newInstance(it, onMoveListener, onFinishListener))
        }
    }

    fun addFragment(fragment: ChilliStoryFragment) {
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getFragmentByPosition(position: Int) = fragments[position]
}
