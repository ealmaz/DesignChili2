package com.design2.chili2.view.stories.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.design2.chili2.view.stories.ChilliStoryBlock
import com.design2.chili2.view.stories.ChilliStoryFragment
import com.design2.chili2.view.stories.StoryListener

class ChilliStoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<Fragment>()

    fun createViewPager(stories: List<ChilliStoryBlock>, storyListener: StoryListener) {
        stories.forEach {
            addFragment(ChilliStoryFragment.newInstance(it, storyListener))
        }
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
