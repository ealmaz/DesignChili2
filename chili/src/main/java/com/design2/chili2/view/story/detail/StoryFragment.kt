package com.design2.chili2.view.story.detail

import android.app.Activity
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        detailedStories.forEachIndexed { index, story -> if (!story.isViewed) return index }
        return DEFAULT
    }

    private fun setViewsByDetailedStory(index: Int) {
        detailedStories[index].let { story ->
            vb.title.text = story.title
            vb.subtitle.text = story.description
            vb.description.text = story.description
            setTextGradient()
            context?.let {
                changeForeground(ContextCompat.getDrawable(it, R.drawable.chili_bg_shading)) }
            setupButton(story.button)
            loadImage(story.image)
        }
    }

    private fun showSubtitle() = with(vb) {
        isSubtitleVisible = true
        setTextGradient()
        context?.let {
            changeForeground(ContextCompat.getDrawable(it, R.drawable.chili_bg_shading)) }

        animateSubtitle()
        progressBar.resumeAnimation()
    }

    private fun animateSubtitle() = with(vb) {
        animateTranslationY(
            view = description,
            valueY = description.height.toFloat() / DESCRIPTION_GONE_HEIGHT,
            duration = SUBTITLE_DURATION,
            valueAlpha = DEFAULT_ANIMATION_VALUE
        ) {
            subtitle.visibility = View.VISIBLE
            description.visibility = View.GONE
            isDescVisible = false
        }
    }

    private fun showDescription() = with(vb) {
        isSubtitleVisible = false
        subtitle.visibility = View.GONE
        context?.let {
            changeForeground(ContextCompat.getDrawable(it, R.drawable.chili_bg_semi_transparent)) }

        animateDescription()
        vb.progressBar.pauseAnimation()
    }

    private fun animateDescription() = with(vb) {
        title.apply {
            translationY = title.height.toFloat() / TITLE_SHOW_HEIGHT
            animate()
                .translationY(DEFAULT_ANIMATION_VALUE)
                .setDuration(SUBTITLE_DURATION)
                .start()
        }

        description.apply {
            visibility = View.VISIBLE
            translationY = title.height.toFloat() / DESCRIPTION_SHOW_HEIGHT
            alpha = DEFAULT_ANIMATION_VALUE
            animate()
                .translationY(DEFAULT_ANIMATION_VALUE)
                .alpha(DESCRIPTION_ALPHA_END)
                .setDuration(DESCRIPTION_DURATION)
                .start()
        }
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
        if (!isDescVisible) {
            if (vb.progressBar.currentIndex == 0)
                listener?.goToPrevFragment()
            else showPreviousStory()
        }
    }

    override fun onTapRight() {
        if (!isDescVisible) {
            if (vb.progressBar.currentIndex == detailedStories.size - 1)
                listener?.goToNextFragment()
            else showNextStory()
        }
    }

    override fun onSwipeLeft() {}

    override fun onSwipeRight() {}

    override fun onSwipeUp(showDescription: Boolean) {
        if (!detailedStories[vb.progressBar.currentIndex].description.isNullOrEmpty()
            && !isDescVisible && showDescription
        ) {
            isDescVisible = true
            showDescription()
        }
    }

    override fun onSwipeDown(deltaY: Float) {
        notifyViewPagerSwipes(false)

        if (!isSubtitleVisible) {
            isDescShownDuringSwipe = true
            showSubtitle()
        } else performSwipeDownAction(deltaY)
    }

    override fun onSwipeDownEnd(isEnoughDragging: Boolean, isSwipeUp: Boolean) {
        notifyViewPagerSwipes(true)

        if (isEnoughDragging && !isSwipeUp) {
            if (isSubtitleVisible && !isDescShownDuringSwipe) {
                vb.progressBar.resetAllAnimations()
                listener?.closingStory()
            }
            isDescShownDuringSwipe = false
        } else returnStory()
    }

    private fun performSwipeDownAction(deltaY: Float) {
        if (!isDescShownDuringSwipe) {
            vb.progressBar.pauseAnimation()
            animateTranslationY(vb.root, vb.root.translationY + deltaY, 0) {}
        }
    }

    private fun showNextStory() = vb.progressBar.completeAnimation()

    private fun showPreviousStory() = with(vb.progressBar) {
        updateCurrentIndex(currentIndex - 1)
        resetAllAnimations()
        startAnimation()
        setViewsByDetailedStory(currentIndex)
    }

    private fun returnStory() {
        if (!isDescVisible) {
            vb.progressBar.resumeAnimation()
            animateTranslationY(vb.root, DEFAULT_ANIMATION_VALUE, RETURN_STORY_DURATION) {}
        }
    }

    private fun animateTranslationY(
        view: View,
        valueY: Float,
        duration: Long,
        valueAlpha: Float = 1f,
        endAnim: () -> Unit
    ) = view.animate()
        .alpha(valueAlpha)
        .translationY(valueY)
        .setInterpolator(DecelerateInterpolator())
        .setDuration(duration)
        .withEndAction(endAnim)
        .start()

    private fun notifyViewPagerSwipes(isEnabled: Boolean) = listener?.viewPagerSwipes(isEnabled)

    private fun changeForeground(drawable: Drawable?) { vb.overlayLayout.foreground = drawable }

    override fun onResume() {
        super.onResume()
        vb.progressBar.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        showSubtitle()
        vb.progressBar.resetAnimation()
    }

    override fun onStop() {
        super.onStop()
        vb.progressBar.resetAnimation()
    }

    companion object {

        const val DEFAULT = 0
        const val DEFAULT_ANIMATION_VALUE = 0f
        const val TITLE_SHOW_HEIGHT = 2
        const val SUBTITLE_DURATION = 300L
        const val DESCRIPTION_GONE_HEIGHT = 50
        const val DESCRIPTION_SHOW_HEIGHT = 6
        const val DESCRIPTION_DURATION = 500L
        const val DESCRIPTION_ALPHA_END = 1f
        const val RETURN_STORY_DURATION = 200L

        fun newInstance(story: Story, callbackListener: StoryCallbackListener): StoryFragment {
            val fragment = StoryFragment()
            fragment.story = story
            fragment.listener = callbackListener
            return fragment
        }
    }
}
