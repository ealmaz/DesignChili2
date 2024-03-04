package com.design2.chili2.view.story.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.design2.chili2.view.story.Story
import com.design2.chili2.view.story.StoryCallbackListener

class StoriesPagerAdapter(
    fragmentActivity: FragmentActivity,
    private var storiesList: List<Story>,
    private val callbackListener: StoryCallbackListener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return storiesList.size
    }

    override fun createFragment(position: Int): Fragment {
        return StoryFragment.newInstance(storiesList[position], callbackListener)
    }
}
