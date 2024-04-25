package com.design2.chili2.view.stories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.design2.chili2.databinding.FragmentStoryBinding

class ChilliStoryFragment: Fragment() {

    private lateinit var storyBlock: ChilliStoryBlock

    private var isCreated: Boolean = false

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
            storyBlock = it.getSerializable(ARG_STORY_BLOCK) as ChilliStoryBlock
        }

        if (isVisible) {
            setupViews(storyBlock)
            isCreated = true
        }
    }

    private fun setupViews(storyBlock: ChilliStoryBlock) {
        storyBlock.stories?.let {
            binding.storyView.setupStories(
                stories = it,
                onMoveListener,
                onFinishListener
            )
        }
    }

    override fun onPause() {
        super.onPause()
        binding.storyView.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        when {
            isVisible && !isCreated -> {
                setupViews(storyBlock)
                isCreated = true
            }
            isVisible -> binding.storyView.resumeTimer()
        }
    }

    companion object {
        private const val ARG_STORY_BLOCK = "story_block"
        private var onMoveListener: StoryMoveListener? = null
        private var onFinishListener: StoryOnFinishListener? = null

        fun newInstance(storyBlock: ChilliStoryBlock, onMoveListener: StoryMoveListener, onFinishListener: StoryOnFinishListener): ChilliStoryFragment {
            return ChilliStoryFragment().apply {
                this@Companion.onMoveListener = onMoveListener
                this@Companion.onFinishListener = onFinishListener
                arguments = Bundle().apply {
                    putSerializable(ARG_STORY_BLOCK, storyBlock)
                }
            }
        }
    }
}