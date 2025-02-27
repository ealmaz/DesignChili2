package com.design2.chili2.view.stories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.design2.chili2.databinding.FragmentStoryBinding

class ChilliStoryFragment: Fragment() {

    private lateinit var storyBlock: ChilliStoryBlock

    private var isCreated: Boolean = false

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            storyBlock = it.getSerializable(ARG_STORY_BLOCK) as ChilliStoryBlock
        }

        setupViewsIfVisible()
    }

    private fun setupViews(storyBlock: ChilliStoryBlock) {
        storyBlock.stories?.let {
            binding.storyView.setupStories(
                stories = it,
                onMoveListener,
                onFinishListener,
                onClickListener,
                storyBlock.blockType
            )
        }
    }

    fun onCloseStories() {
        if (isAdded && _binding != null) {
            binding.storyView.finishStories()
        }
    }

    override fun onPause() {
        super.onPause()
        if (_binding != null) binding.storyView.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        when {
            isFragmentVisible() && !isCreated -> {
                setupViews(storyBlock)
                isCreated = true
            }

            isVisible -> binding.storyView.resumeTimer()
        }
    }

    private fun isFragmentVisible(): Boolean {
        return if (isResumed) viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) else false
    }

    private fun setupViewsIfVisible() {
        if (isResumed && isVisible) {
            setupViews(storyBlock)
            isCreated = true
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        isCreated = false
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        private const val ARG_STORY_BLOCK = "story_block"
        private var onMoveListener: StoryMoveListener? = null
        private var onFinishListener: StoryOnFinishListener? = null
        private var onClickListener: StoryClickListener? = null

        fun newInstance(storyBlock: ChilliStoryBlock, onMoveListener: StoryMoveListener?, onFinishListener: StoryOnFinishListener?, onStoryClickListener: StoryClickListener?): ChilliStoryFragment {
            return ChilliStoryFragment().apply {
                this@Companion.onMoveListener = onMoveListener
                this@Companion.onFinishListener = onFinishListener
                this@Companion.onClickListener = onStoryClickListener
                arguments = Bundle().apply {
                    putSerializable(ARG_STORY_BLOCK, storyBlock)
                }
            }
        }
    }
}