package com.design2.chili2.view.story.detail

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
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
    private var isSubtitleVisible: Boolean = true
    private var isDescVisible: Boolean = false
    private var isDescShownDuringSwipe = false

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
        btnClose.setOnClickListener { listener?.closingStory() }
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
            vb.subtitle.text = "Some Subtitle"
            setTextGradient()
            context?.let { vb.overlayLayout.foreground = ContextCompat.getDrawable(it, R.drawable.chili_bg_shading) }
            setupButton(story.button)
            vb.description.text = story.description
            loadImage(story.image)
        }
    }

    private fun showSubtitle() = with(vb) {
        isSubtitleVisible = true

        title.animate()
            .translationY(0f)
            .setDuration(250) // Убедитесь, что продолжительность совпадает с анимацией description
            .start()

        description.animate()
            .alpha(0f)
            .translationY(description.height.toFloat())
            .setDuration(200)
            .withEndAction {
                subtitle.visibility = View.VISIBLE
                description.visibility = View.GONE
                isDescVisible= false
            }.start()

        setTextGradient()
        context?.let { overlayLayout.foreground = ContextCompat.getDrawable(it, R.drawable.chili_bg_shading) }
        progressBar.resumeAnimation()
    }

    private fun showDescription() = with(vb) {
        isSubtitleVisible = false

        subtitle.visibility = View.GONE

        title.apply {
            visibility = View.VISIBLE
            translationY = title.height.toFloat() / 2
            animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }

        description.apply {
            visibility = View.VISIBLE
            translationY = title.height.toFloat() / 6
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }

        context?.let { overlayLayout.foreground = ContextCompat.getDrawable(it, R.drawable.chili_bg_semi_transparent) }
        vb.progressBar.pauseAnimation()
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

    override fun onLongPressStart() { vb.progressBar.pauseAnimation() }

    override fun onLongPressRelease() { vb.progressBar.resumeAnimation() }

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

    override fun onSwipeLeft() { listener?.goToPrevFragment() }

    override fun onSwipeRight() { listener?.goToNextFragment() }

    override fun onSwipeUp() {
        if (!detailedStories[vb.progressBar.currentIndex].description.isNullOrEmpty() && !isDescVisible) {
            isDescVisible = true
            showDescription()
        }
    }

    override fun onSwipeDown(deltaY: Float) {
        listener?.viewPagerSwipes(false)
        if (!isSubtitleVisible ) {
            isDescShownDuringSwipe = true
            showSubtitle()
        } else {
            performSwipeDownAction(deltaY)
        }
    }

    override fun onSwipeDownEnd(isEnoughDragging: Boolean) {
        listener?.viewPagerSwipes(true)
        if (isEnoughDragging) {
            vb.progressBar.resetAllAnimations()
            if (isSubtitleVisible && !isDescShownDuringSwipe) { listener?.closingStory() }
            isDescShownDuringSwipe = false
        } else returnStory()
    }

    private fun performSwipeDownAction(deltaY: Float) {
        if (!isDescShownDuringSwipe) {
            vb.root.animate()
                .translationY(vb.root.translationY + deltaY)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(0)
                .start()
        }
    }

    private fun showNextStory() = vb.progressBar.completeAnimation()

    private fun showPreviousStory() = with(vb.progressBar) {
        updateCurrentIndex(currentIndex - 1)
        resetAnimation()
        startAnimation()
        setViewsByDetailedStory(currentIndex)
    }

    private fun returnStory() =
        vb.root.animate()
            .translationY(0f)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(500)
            .start()

    override fun onResume() {
        super.onResume()
        vb.progressBar.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        vb.progressBar.resetAnimation()
    }

    override fun onStop() {
        super.onStop()
        vb.progressBar.resetAnimation()
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
