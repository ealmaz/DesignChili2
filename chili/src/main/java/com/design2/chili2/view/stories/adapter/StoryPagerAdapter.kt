package com.design2.chili2.view.stories.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.design2.chili2.view.stories.StoryBlock
import com.design2.chili2.view.stories.StoryFragment
import com.design2.chili2.view.stories.StoryListener

class StoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<Fragment>()

    fun createViewPager(stories: List<StoryBlock>, storyListener: StoryListener) {
        stories.forEach {
            addFragment(StoryFragment.newInstance(it, storyListener))
        }
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
