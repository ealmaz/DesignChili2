package com.design2.chili2.view.stories.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.design2.chili2.view.stories.PageListener
import com.design2.chili2.view.stories.StoryBlock
import com.design2.chili2.view.stories.StoryFragment

class StoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<Fragment>()

    fun createViewPager(stories: List<StoryBlock>, pageListener: PageListener) {
        stories.forEach {
            addFragment(StoryFragment.newInstance(it, pageListener))
        }
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    fun pauseAllFragments() {
        fragments.forEach {
            if (it.isAdded) it.onPause()
        }
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
