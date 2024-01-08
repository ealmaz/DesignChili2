package com.design2.chili2.view.story.detail

import android.app.Activity
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoryBinding
import com.design2.chili2.extensions.dp
import com.design2.chili2.view.common.GesturesListener
import com.design2.chili2.view.story.ButtonModel
import com.design2.chili2.view.story.DetailedStory
import com.design2.chili2.view.story.ProgressBarListener
import com.design2.chili2.view.story.Story
import com.design2.chili2.view.story.StoryCallbackListener

class StoryFragment : Fragment(), ProgressBarListener, GesturesListener {
    private var story: Story? = null
    private var detailedStories = listOf<DetailedStory>()
    private var listener: StoryCallbackListener? = null

    private lateinit var vb: ChiliViewStoryBinding
    private var isSubtitleVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = ChiliViewStoryBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        story?.stories?.let { stories ->
            detailedStories = stories.sortedBy { it.orderNumber }
        }
        setupViews()
    }

    private fun setupViews() {
        vb.progressBar.initProgressBar(detailedStories.size)
        setupViewByStory()
        setViewsByDetailedStory(getStoryIndexToShow())
        setupListeners()
    }

    private fun setupListeners() = with(vb) {
        gesturesView.setGestureListener(this@StoryFragment)
        progressBar.setProgressBarListener(this@StoryFragment)
        btnClose.setOnClickListener { listener?.closeStory() }
    }

    private fun getStoryIndexToShow(): Int {
        detailedStories.forEachIndexed { index, story ->
            if (!story.isViewed)
                return index
        }
        return 0
    }

    private fun setViewsByDetailedStory(index: Int) {
        detailedStories[index].let { story ->
            vb.title.text = story.title
            vb.subtitle.text = story.description
            showSubtitle()
            setupButton(story.button)
            vb.description.text = story.description
            loadImage(story.image)
        }
    }

    private fun showSubtitle() = with(vb) {
        isSubtitleVisible = true
        subtitle.isVisible = true
        description.isVisible = false
        setTextGradient()
        context?.let { overlayLayout.foreground = ContextCompat.getDrawable(it, R.drawable.chili_bg_shading) }
        progressBar.resumeAnimation()
    }

    private fun setTextGradient() {
        val gradient = context?.let {
            LinearGradient(
                0f, 0f, 0f, 68.dp.toFloat(),
                it.getColor(R.color.white_1),
                it.getColor(R.color.white_alpha_0),
                Shader.TileMode.CLAMP
            )
        }
        vb.subtitle.paint.shader = gradient
    }

    private fun setupButton(button: ButtonModel?) {
        vb.btnDetails.apply {
            button?.title?.let { text = it }
            button?.titleColor?.let { setTextColor(Color.parseColor(it)) }
            button?.backgroundColor?.let { setBackgroundColor(Color.parseColor(it)) }
            button?.deepLink?.let { setOnClickListener { listener?.openDeeplink(button.deepLink) } }
        }
    }

    private fun loadImage(imageUrl: String?) {
        context?.let {
            Glide.with(context as Activity)
                .load(imageUrl)
                .centerCrop()
                .into(vb.storyImage)
        }
    }

    private fun setupViewByStory() {
        if (story?.isMarketingCenter == true) {
            vb.badge.isVisible = true
            vb.borderView.setBackgroundResource(R.drawable.chili_story_border)
        }
    }

    override fun onAnimationFinishedAt(index: Int) {
        if (index + 1 < detailedStories.size) {
            setViewsByDetailedStory(index + 1)
        } else listener?.goToNextFragment()
    }

    override fun onLongPressStart() {
        vb.progressBar.pauseAnimation()
    }

    override fun onLongPressRelease() {
        vb.progressBar.resumeAnimation()
    }

    override fun onTapLeft() {
        if (vb.progressBar.currentIndex == 0)
            listener?.goToPrevFragment()
        else showPreviousStory()
    }

    override fun onTapRight() {
        if (vb.progressBar.currentIndex == detailedStories.size - 1)
            listener?.goToNextFragment()
        else showNextStory()
    }

    override fun onSwipeLeft() {
        listener?.goToPrevFragment()
    }

    override fun onSwipeRight() {
        listener?.goToPrevFragment()
    }

    override fun onSwipeUp() {
        if (!detailedStories[vb.progressBar.currentIndex].description.isNullOrEmpty())
            showDescription()
    }

    override fun onSwipeDown() {
        if (isSubtitleVisible) listener?.closeStory()
        else showSubtitle()
    }

    private fun showNextStory() {
        vb.progressBar.completeAnimation()
    }

    private fun showPreviousStory() = with(vb.progressBar) {
        resetAnimation()
        updateCurrentIndex(currentIndex - 1)
        resetAnimation()
        startAnimation()
        setViewsByDetailedStory(currentIndex)
    }

    private fun showDescription() = with(vb) {
        isSubtitleVisible = false
        description.isVisible = true
        subtitle.isVisible = false
        context?.let { overlayLayout.foreground = ContextCompat.getDrawable(it, R.drawable.chili_bg_semi_transparent) }
        vb.progressBar.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        vb.progressBar.resumeAnimation()
    }

    override fun onPause() {
        super.onPause()
        vb.progressBar.pauseAnimation()
    }

    companion object {
        fun newInstance(story: Story, callbackListener: StoryCallbackListener): StoryFragment {
            val fragment = StoryFragment()
            fragment.story = story
            fragment.listener = callbackListener
            return fragment
        }
    }
}