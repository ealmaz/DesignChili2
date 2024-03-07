package com.design2.chili2.view.stories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.design2.chili2.R
import com.design2.chili2.databinding.FragmentStoryBinding

class StoryFragment: Fragment() {

    private lateinit var storyBlock: StoryBlock

    private lateinit var binding: FragmentStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            storyBlock = it.getSerializable(ARG_STORY_BLOCK) as StoryBlock
        }

        setupViews(storyBlock)
    }

    private fun setupViews(storyBlock: StoryBlock) {
        storyBlock.stories?.let {
            binding.storyView.setupStories(
                stories = it,
                listener
            )
        }
    }

    override fun onPause() {
        super.onPause()
        binding.storyView.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        binding.storyView.resumeTimer()
    }

    companion object {
        private const val ARG_STORY_BLOCK = "story_block"
        private var listener: StoryListener? = null

        fun newInstance(storyBlock: StoryBlock, listener: StoryListener): StoryFragment {
            return StoryFragment().apply {
                this@Companion.listener = listener
                arguments = Bundle().apply {
                    putSerializable(ARG_STORY_BLOCK, storyBlock)
                }
            }
        }
    }
}